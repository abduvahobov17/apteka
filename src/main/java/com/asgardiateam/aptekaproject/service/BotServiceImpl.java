package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.entity.BucketProduct;
import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.BucketProductStatus;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.service.interfaces.*;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.asgardiateam.aptekaproject.constants.MessageKey.*;
import static com.asgardiateam.aptekaproject.enums.BotState.*;
import static com.asgardiateam.aptekaproject.enums.Lang.RU;
import static com.asgardiateam.aptekaproject.enums.Lang.tryFindLang;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final UserService userService;
    private final ProductService productService;
    private final BucketService bucketService;
    private final BucketProductService bucketProductService;

    Map<BotState, BiFunction<Update, User, SendMessage>> sendMessageMethods = new ConcurrentHashMap<>();

    Map<BotState, BiFunction<CallbackQuery, Lang, EditMessageReplyMarkup>> editReplyMarkup = new ConcurrentHashMap<>();

    {
        sendMessageMethods.put(START, this::greet);
        sendMessageMethods.put(REGISTER_LANG, this::registerLanguage);
        sendMessageMethods.put(REGISTER_PHONE, this::registerPhone);
        sendMessageMethods.put(REGISTER_NAME, this::registerName);
        sendMessageMethods.put(MAIN_MENU, this::mainMenu);
        sendMessageMethods.put(SEARCH_PRODUCT_START, this::searchStart);
        sendMessageMethods.put(SEARCH_PRODUCT_PROGRESS, this::searchProgress);
    }

    {
        editReplyMarkup.put(SEARCH_PRODUCT_PROGRESS, this::updateCallBackQueryProductOrder);
    }

    @Override
    public BotApiMethod<? extends Serializable> executeMethod(Update update, User user) {
        if(update.hasCallbackQuery())
            return editReplyMarkup.get(user.getBotState()).apply(update.getCallbackQuery(), user.getLang());
        else
            return sendMessageMethods.get(user.getBotState()).apply(update, user);
    }

    public SendMessage greet(Update update, User user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId(update));

        user.setFirstNameTeleg(update.getMessage().getFrom().getFirstName());
        user.setLastNameTeleg(update.getMessage().getFrom().getLastName());
        user.setUserNameTeleg(update.getMessage().getFrom().getUserName());
        user.setBotState(BotState.REGISTER_LANG);
        userService.save(user);
        sendMessage.setText(String.format(GREET, user.getFirstNameTeleg(), user.getFirstNameTeleg()));

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("Русский язык \uD83C\uDDF7\uD83C\uDDFA");
        row.add(button);
        rows.add(row);
        row = new KeyboardRow();
        button = new KeyboardButton("O'zbek tili \uD83C\uDDFA\uD83C\uDDFF");
        row.add(button);
        rows.add(row);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage registerLanguage(Update update, User user) {
        String chatId = getChatId(update);
        String language = update.getMessage().getText();

        if (isNull(tryFindLang(language)))
            return new SendMessage(chatId, "Tilni tanlang❗️\nВыберите язык❗️");

        user.setBotState(BotState.REGISTER_NAME);
        user.setLang(Lang.findByDescription(update.getMessage().getText()));
        userService.save(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(user.getLang().equals(Lang.RU) ? SEND_NAME_RU : SEND_NAME_UZ);
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    public SendMessage registerName(Update update, User user) {

        String chatId = getChatId(update);
        boolean isRu = user.getLang().equals(RU);
        String[] fullName = update.getMessage().getText().trim().toUpperCase().split(" ");
        String firstName = fullName[0];
        String lastName = fullName[1];

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBotState(REGISTER_PHONE);
        userService.save(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(isRu ? SEND_PHONE_RU : SEND_PHONE_UZ);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(user.getLang().equals(Lang.RU) ? "Поделиться контактом \uD83D\uDCDE" : "Telefon raqamni yuborish \uD83D\uDCDE");
        button.setRequestContact(true);
        row.add(button);
        rows.add(row);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage registerPhone(Update update, User user) {
        String chatId = getChatId(update);
        Contact contact = update.getMessage().getContact();
        boolean isRu = user.getLang().equals(RU);
        String phoneNumber = contact.getPhoneNumber();
        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;
        user.setPhoneNumber(phoneNumber);
        user.setBotState(MAIN_MENU);
        user.setClientType(ClientType.REGISTERED);
        userService.save(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(isRu ? SUCCESS_REGISTRATION_RU : SUCCESS_REGISTRATION_UZ);
        sendMessage.setChatId(chatId);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(isRu ? SETTINGS_RU : SETTINGS_UZ);
        row.add(button);
        rows.add(row);
        row = new KeyboardRow();
        button = new KeyboardButton();
        button.setText(isRu ? SEARCH_PRODUCT_RU : SEARCH_PRODUCT_UZ);
        row.add(button);
        rows.add(row);

        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage mainMenu(Update update, User user) {
        String chatId = getChatId(update);
        String text = update.getMessage().getText();
        boolean isRu = user.getLang().equals(RU);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (text.equals(SEARCH_PRODUCT_RU) || text.equals(SEARCH_PRODUCT_UZ)) {
            user.setBotState(SEARCH_PRODUCT_START);
            sendMessage.setText(isRu ? SEARCH_PRODUCT_START_RU : SEARCH_PRODUCT_START_UZ);
        } else if (text.equals(SETTINGS_RU) || text.equals(SETTINGS_UZ)) {
            user.setBotState(SETTINGS);
            sendMessage.setText(isRu ? SETTINGS_START_RU : SETTINGS_START_UZ);
        } else {
            sendMessage.setText(isRu ? CHOOSE_ERROR_RU : CHOOSE_ERROR_UZ);
        }
        userService.save(user);
        return sendMessage;
    }

    public SendMessage searchStart(Update update, User user) {
        String chatId = getChatId(update);
        boolean isRu = user.getLang().equals(RU);
        String productName = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (productName.length() < 3) {
            sendMessage.setText(user.getLang().equals(RU) ? INVALID_DATA_RU : INVALID_DATA_UZ);
            return sendMessage;
        }

        PageDto<ProductDTO> allProducts = productService.getAll(Pageable.unpaged(), ProductCriteria.builder().name(productName).fromAmount(0L).fromPrice(0L).build());

        if (allProducts.getItems().isEmpty()) {
            sendMessage.setText(isRu ? PRODUCT_NOT_FOUND_RU : PRODUCT_NOT_FOUND_UZ);
            return sendMessage;
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        getAllProductList(replyKeyboardMarkup, user.getLang(), allProducts);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(isRu ? CHOOSE_PRODUCT_RU : CHOOSE_PRODUCT_UZ);
        user.setBotState(SEARCH_PRODUCT_PROGRESS);
        userService.save(user);
        return sendMessage;
    }

    public SendMessage searchProgress(Update update, User user) {
        String chatId = getChatId(update);
        String action = update.getMessage().getText();
        boolean isRu = user.getLang().equals(RU);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (action.equals(CANCEL_RU) || action.equals(CANCEL_UZ)) {
            sendMessage = registerPhone(update, user);
            sendMessage.setText(isRu ? THANKS_FOR_USING_BOT_RU : THANKS_FOR_USING_BOT_UZ);
            return sendMessage;
        }

        Optional<Product> product = productService.findByName(action);
        if (product.isEmpty()) {
            sendMessage.setText(isRu ? PRODUCT_NOT_FOUND_RU : PRODUCT_NOT_FOUND_UZ);
            return sendMessage;
        }

        Bucket bucket = bucketService.getBucketByUserId(user.getId())
                .orElseGet(Bucket::new);
        bucket = bucketService.save(bucket);

        BucketProduct bucketProduct = new BucketProduct();
        bucket.getBucketProducts().add(bucketProduct);
        bucketProduct.setBucket(bucket);
        bucketProduct.setProduct(product.get());
        bucketProduct.setAmount(0L);
        BucketProduct savedBucketProduct = bucketProductService.save(bucketProduct);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listListButton = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        InlineKeyboardButton minus = new InlineKeyboardButton();
        minus.setText(MINUS);
        minus.setCallbackData(MINUS + "/" + savedBucketProduct.getId());
        inlineKeyboardButtons.add(minus);
        InlineKeyboardButton amount = new InlineKeyboardButton();
        amount.setText(String.valueOf(savedBucketProduct.getAmount()));
        amount.setCallbackData("hello");
        inlineKeyboardButtons.add(amount);
        InlineKeyboardButton plus = new InlineKeyboardButton();
        plus.setText(PLUS);
        plus.setCallbackData(PLUS + "/" + savedBucketProduct.getId());
        inlineKeyboardButtons.add(plus);
        listListButton.add(inlineKeyboardButtons);

        List<InlineKeyboardButton> inlineKeyboardButtonsBucket = new ArrayList<>();
        InlineKeyboardButton toBucket = new InlineKeyboardButton();
        toBucket.setText(isRu
                ? (TO_BUCKET_RU)
                : (TO_BUCKET_UZ));
        toBucket.setCallbackData(isRu
                ? (TO_BUCKET_RU+"/"+bucketProduct.getId())
                : (TO_BUCKET_UZ+"/"+bucketProduct.getId()));
        inlineKeyboardButtonsBucket.add(toBucket);
        listListButton.add(inlineKeyboardButtonsBucket);

        inlineKeyboardMarkup.setKeyboard(listListButton);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(getProductDescription(user.getLang(), product.get()));
        return sendMessage;
    }

    private String getChatId(Update update) {
        return String.valueOf(update.getMessage().getChatId());
    }

    private void getAllProductList(ReplyKeyboardMarkup replyKeyboardMarkup, Lang lang, PageDto<ProductDTO> allProducts) {
        List<KeyboardRow> rows = new ArrayList<>();
        for (ProductDTO product : allProducts.getItems()) {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton();
            button.setText(product.getName());
            row.add(button);
            rows.add(row);
        }

        KeyboardRow row = new KeyboardRow();
        KeyboardButton cancel = new KeyboardButton(lang.equals(RU) ? CANCEL_RU : CANCEL_UZ);
        row.add(cancel);
        rows.add(row);
        replyKeyboardMarkup.setKeyboard(rows);
    }

    private String getProductDescription(Lang lang, Product product) {
        return lang.equals(RU) ? PRODUCT_NAME_RU + ": " + product.getName() + "\n" +
                PRODUCT_DESCRIPTION_RU + ": " + product.getDescription()
                : PRODUCT_NAME_UZ + ": " + product.getName() + "\n" +
                PRODUCT_DESCRIPTION_UZ + ": " + product.getDescription();
    }

    public EditMessageReplyMarkup updateCallBackQueryProductOrder(CallbackQuery callbackQuery, Lang lang) {
        String data = callbackQuery.getData();
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));

        BucketProduct bucketProduct = bucketProductService.findById(Long.valueOf(data.split("/")[1]));

        List<InlineKeyboardButton> bucketButton = new ArrayList<>();

        if (data.contains(PLUS))
            bucketProduct.setAmount(bucketProduct.getAmount() + 1);

        if (data.contains(MINUS) && bucketProduct.getAmount() > 0)
            bucketProduct.setAmount(bucketProduct.getAmount() - 1);
        if (data.contains(TO_BUCKET_RU) || data.contains(TO_BUCKET_UZ)) {
            bucketProduct.setStatus(BucketProductStatus.CHOSEN);
            bucketButton = new ArrayList<>();
            InlineKeyboardButton toBucket = new InlineKeyboardButton();
            toBucket.setText(data.contains(TO_BUCKET_RU)
                    ? (IN_BUCKET_RU)
                    : (IN_BUCKET_UZ));
            toBucket.setCallbackData(data.contains(TO_BUCKET_RU)
                    ? (IN_BUCKET_RU+"/"+bucketProduct.getId())
                    : (IN_BUCKET_UZ+"/"+bucketProduct.getId()));
            bucketButton.add(toBucket);
        } else if (data.contains(IN_BUCKET_RU) || data.contains(IN_BUCKET_UZ)) {
            bucketProduct.setStatus(BucketProductStatus.REVIEWED);
            bucketButton = new ArrayList<>();
            InlineKeyboardButton toBucket = new InlineKeyboardButton();
            toBucket.setText(data.contains(IN_BUCKET_RU)
                    ? (TO_BUCKET_RU)
                    : (TO_BUCKET_UZ));
            toBucket.setCallbackData(data.contains(IN_BUCKET_RU)
                    ? (TO_BUCKET_RU+"/"+bucketProduct.getId())
                    : (TO_BUCKET_UZ+"/"+bucketProduct.getId()));
            bucketButton.add(toBucket);
        } else {
            bucketButton = new ArrayList<>();
            InlineKeyboardButton toBucket = new InlineKeyboardButton();
            toBucket.setText(lang.equals(RU)
                    ? (TO_BUCKET_RU)
                    : (TO_BUCKET_UZ));
            toBucket.setCallbackData(lang.equals(RU)
                    ? (TO_BUCKET_RU+"/"+bucketProduct.getId())
                    : (TO_BUCKET_UZ+"/"+bucketProduct.getId()));
            bucketButton.add(toBucket);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listListButton = new ArrayList<>();

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

        InlineKeyboardButton minus = new InlineKeyboardButton();
        minus.setText(MINUS);
        minus.setCallbackData(MINUS + "/" + bucketProduct.getId());
        inlineKeyboardButtons.add(minus);
        InlineKeyboardButton amount = new InlineKeyboardButton();
        amount.setText(String.valueOf(bucketProduct.getAmount()));
        amount.setCallbackData("hello");
        inlineKeyboardButtons.add(amount);
        InlineKeyboardButton plus = new InlineKeyboardButton();
        plus.setText(PLUS);
        plus.setCallbackData(PLUS + "/" + bucketProduct.getId());
        inlineKeyboardButtons.add(plus);
        listListButton.add(inlineKeyboardButtons);
        listListButton.add(bucketButton);

        inlineKeyboardMarkup.setKeyboard(listListButton);

        bucketProductService.save(bucketProduct);

        editMessageReplyMarkup.setReplyMarkup(inlineKeyboardMarkup);
        editMessageReplyMarkup.setInlineMessageId(callbackQuery.getInlineMessageId());
        editMessageReplyMarkup.setMessageId(callbackQuery.getMessage().getMessageId());

        return editMessageReplyMarkup;
    }

}
