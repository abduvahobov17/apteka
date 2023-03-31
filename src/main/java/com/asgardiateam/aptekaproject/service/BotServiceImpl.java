package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.entity.BucketProduct;
import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.enums.*;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.service.interfaces.*;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

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

    Map<BotState, BiFunction<Update, User, BotApiMethod<? extends Serializable>>> sendMessageMethods = new ConcurrentHashMap<>();

    Map<BotState, BiFunction<CallbackQuery, User, BotApiMethod<? extends Serializable>>> editReplyMarkup = new ConcurrentHashMap<>();

    {
        sendMessageMethods.put(START, this::greet);
        sendMessageMethods.put(REGISTER_LANG, this::registerLanguage);
        sendMessageMethods.put(REGISTER_PHONE, this::registerPhone);
        sendMessageMethods.put(REGISTER_NAME, this::registerName);
        sendMessageMethods.put(MAIN_MENU, this::mainMenu);
        sendMessageMethods.put(SEARCH_PRODUCT_START, this::searchStart);
        sendMessageMethods.put(SEARCH_PRODUCT_PROGRESS, this::searchProgress);
        sendMessageMethods.put(VIEW_PRODUCT, this::viewProductStage);
        sendMessageMethods.put(DELIVERY_START, this::deliveryStart);
        sendMessageMethods.put(DELIVERY_PROGRESS, this::deliveryProgress);
        sendMessageMethods.put(DELIVERY_PROGRESS_LOCATION, this::deliveryLocation);
    }

    {
        editReplyMarkup.put(SEARCH_PRODUCT_PROGRESS, this::updateCallBackQueryProductOrder);
        editReplyMarkup.put(VIEW_PRODUCT, this::updateMarkupProductsReview);
    }

    @Override
    public BotApiMethod<? extends Serializable> executeMethod(Update update, User user) {
        if(update.hasCallbackQuery())
            return editReplyMarkup.get(user.getBotState()).apply(update.getCallbackQuery(), user);
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
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(isRu ? SEARCH_PRODUCT_RU : SEARCH_PRODUCT_UZ);
        row.add(button);
        rows.add(row);
        row = new KeyboardRow();
        button = new KeyboardButton();
        button.setText(isRu ? SETTINGS_RU : SETTINGS_UZ);
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
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(isRu ? CHOOSE_PRODUCT_RU : CHOOSE_PRODUCT_UZ);
        user.setBotState(SEARCH_PRODUCT_PROGRESS);
        userService.save(user);
        return sendMessage;
    }

    public BotApiMethod<? extends Serializable> searchProgress(Update update, User user) {
        String chatId = getChatId(update);
        String action = update.getMessage().getText();
        boolean isRu = user.getLang().equals(RU);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (action.equals(SEARCH_PRODUCT_RU) || action.equals(SEARCH_PRODUCT_UZ)) {
            user.setBotState(SEARCH_PRODUCT_START);
            sendMessage.setText(isRu ? SEARCH_PRODUCT_START_RU : SEARCH_PRODUCT_START_UZ);
            userService.save(user);
            return sendMessage;
        }

        if (action.equals(CANCEL_RU) || action.equals(CANCEL_UZ)) {
            sendMessage.setText(isRu ? THANKS_FOR_USING_BOT_RU : THANKS_FOR_USING_BOT_UZ);
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setOneTimeKeyboard(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            List<KeyboardRow> rows = new ArrayList<>();

            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton();
            button.setText(isRu ? SEARCH_PRODUCT_RU : SEARCH_PRODUCT_UZ);
            row.add(button);
            rows.add(row);
            row = new KeyboardRow();
            button = new KeyboardButton();
            button.setText(isRu ? SETTINGS_RU : SETTINGS_UZ);
            row.add(button);
            rows.add(row);

            replyKeyboardMarkup.setKeyboard(rows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

            user.setBotState(MAIN_MENU);
            userService.save(user);
            return sendMessage;
        }

        if (action.equals(BUCKET_UZ) || action.equals(BUCKET_RU)) {
            Optional<Bucket> bucketByUserId = bucketService.getBucketByUserId(user.getId());

            if (bucketByUserId.isEmpty()) {
                sendMessage.setText("BUCKET IS EMPTY!");
                return sendMessage;
            }

            Bucket bucket = bucketByUserId.get();

            List<BucketProduct> bucketProducts = bucketByUserId.get().getBucketProducts()
                    .stream()
                    .filter(x -> x.getStatus().equals(BucketProductStatus.CHOSEN))
                    .toList();

            if (bucketProducts.isEmpty()) {
                sendMessage.setText("BUCKET IS EMPTY!");
                return sendMessage;
            }

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            String bucketMenu = getBucketMenu(inlineKeyboardMarkup, user.getLang(), bucket.getId(), bucketProducts);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendMessage.setText(bucketMenu);

            user.setBotState(VIEW_PRODUCT);
            userService.save(user);

            return sendMessage;
        }

        Optional<Product> product = productService.findByName(action);
        if (product.isEmpty()) {
            sendMessage.setText(isRu ? PRODUCT_NOT_FOUND_RU : PRODUCT_NOT_FOUND_UZ);
            return sendMessage;
        }


        Bucket bucket = bucketService.getBucketByUserId(user.getId())
                .orElseGet(Bucket::new);
        bucket.setUser(user);
        bucket.setBucketStatus(BucketStatus.PROGRESS);
        bucket = bucketService.save(bucket);

        BucketProduct bucketProduct = new BucketProduct();
        bucket.getBucketProducts().add(bucketProduct);
        bucketProduct.setBucket(bucket);
        bucketProduct.setStatus(BucketProductStatus.REVIEWED);
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

    public SendMessage viewProductStage(Update update, User user) {
        return null;
    }

    public SendMessage deliveryStart(Update update, User user) {
        String chatId = getChatId(update);
        boolean isRu = user.getLang().equals(RU);
        String action = update.getMessage().getText();

        Bucket bucket = bucketService.getBucketByUserId(user.getId()).get();

        if (action.equals(PICKUP_UZ) || action.equals(PICKUP_RU)) {
            user.setBotState(DELIVERY_PROGRESS);
            userService.save(user);
            return pickUpMenu(bucket.getBucketProducts(), isRu, chatId);
        }

        if (action.equals(DELIVERY_UZ) || action.equals(DELIVERY_RU)) {
            user.setBotState(DELIVERY_PROGRESS_LOCATION);
            userService.save(user);
            return getLocation(isRu, chatId);
        }
        return null;
    }

    public SendMessage deliveryLocation(Update update, User user) {
        String chatId = getChatId(update);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Location location = update.getMessage().getLocation();
        if (location == null) {
            sendMessage.setText("SEND LOCATION");
            return sendMessage;
        }

        Bucket bucket = bucketService.getBucketByUserId(user.getId()).get();
        bucket.setLon(location.getLongitude());
        bucket.setLat(location.getLatitude());
        bucketService.save(bucket);

        user.setBotState(DELIVERY_PROGRESS);
        userService.save(user);

        return pickUpMenu(bucket.getBucketProducts(), user.getLang().equals(RU), chatId);
    }

    public SendMessage deliveryProgress(Update update, User user) {
        String chatId = getChatId(update);
        String action = update.getMessage().getText();
        boolean isRu = user.getLang().equals(RU);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(isRu ? SEARCH_PRODUCT_RU : SEARCH_PRODUCT_UZ);
        row.add(button);
        rows.add(row);
        row = new KeyboardRow();
        button = new KeyboardButton();
        button.setText(isRu ? SETTINGS_RU : SETTINGS_UZ);
        row.add(button);
        rows.add(row);

        replyKeyboardMarkup.setKeyboard(rows);

        Bucket bucket = bucketService.getBucketByUserId(user.getId()).get();

        if (action.equals(CONFIRM_UZ) || action.equals(CONFIRM_RU)) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText(isRu ? DELIVERY_CONFIRM_RU : DELIVERY_CONFIRM_UZ);
            bucket.setBucketStatus(BucketStatus.PENDING);
            user.setBotState(MAIN_MENU);
            userService.save(user);
            bucketService.save(bucket);
            return sendMessage;
        }

        if (action.equals(CANCEL_UZ) || action.equals(CANCEL_RU)) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText(isRu ? THANKS_FOR_USING_BOT_RU : THANKS_FOR_USING_BOT_UZ);
            bucket.setBucketStatus(BucketStatus.CANCEL);
            user.setBotState(MAIN_MENU);
            userService.save(user);
            bucketService.save(bucket);
            return sendMessage;
        }

        sendMessage.setText(isRu ? CHOOSE_ERROR_RU : CHOOSE_ERROR_UZ);
        return sendMessage;
    }

    public EditMessageReplyMarkup updateCallBackQueryProductOrder(CallbackQuery callbackQuery, User user) {
        String data = callbackQuery.getData();
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));

        BucketProduct bucketProduct = bucketProductService.findById(Long.valueOf(data.split("/")[1]));

        List<InlineKeyboardButton> bucketButton;

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
            toBucket.setText(user.getLang().equals(RU)
                    ? (TO_BUCKET_RU)
                    : (TO_BUCKET_UZ));
            toBucket.setCallbackData(user.getLang().equals(RU)
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

    public BotApiMethod<? extends Serializable> updateMarkupProductsReview(CallbackQuery callbackQuery, User user) {
        String data = callbackQuery.getData();
        boolean isRu = user.getLang().equals(RU);
        Long bucketProductId = Long.valueOf(data.split("/")[1]);
        Optional<Bucket> bucketByUserId = bucketService.getBucketByUserId(user.getId());

        EditMessageText editMessageText = new EditMessageText();

        if (bucketByUserId.isEmpty())
            throw new AptekaException("BucketIsEmpty");

        Bucket bucket = bucketByUserId.get();

        if (data.contains(CLEAR)) {
            bucketProductService.deleteAll(bucket.getBucketProducts());
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
            sendMessage.setText("Bucket is cleared!");
            user.setBotState(MAIN_MENU);
            userService.save(user);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> rows = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton();
            button.setText(isRu ? SEARCH_PRODUCT_RU : SEARCH_PRODUCT_UZ);
            row.add(button);
            rows.add(row);
            row = new KeyboardRow();
            button = new KeyboardButton();
            button.setText(isRu ? SETTINGS_RU : SETTINGS_UZ);
            row.add(button);
            rows.add(row);

            replyKeyboardMarkup.setKeyboard(rows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            return sendMessage;
        }

        if (data.contains(ORDER)) {
            SendMessage sendMessage = orderMenu(user.getLang());
            user.setBotState(DELIVERY_START);
            sendMessage.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
            userService.save(user);
            return sendMessage;
        }

        BucketProduct bucketProduct = bucket.getBucketProducts()
                .stream()
                .filter(x -> x.getId().equals(bucketProductId))
                .findFirst()
                .orElseThrow(AptekaException::bucketProductNotFound);

        if (data.contains(PLUS)) {
            bucketProduct.setAmount(bucketProduct.getAmount() + 1);
        }
        if (data.contains(MINUS)) {
            if (bucketProduct.getAmount() <= 1) {
                throw new AptekaException(AMOUNT_NOT_VALID);
            }
            bucketProduct.setAmount(bucketProduct.getAmount() - 1);
        }
        if (data.contains(REMOVE)) {
            bucketProduct.setStatus(BucketProductStatus.REVIEWED);
        }


        List<BucketProduct> filtered = bucket.getBucketProducts()
                .stream()
                .filter(x -> x.getStatus().equals(BucketProductStatus.CHOSEN))
                .sorted(Comparator.comparing(BucketProduct::getCreatedDate))
                .toList();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        String bucketMenu = getBucketMenu(inlineKeyboardMarkup, user.getLang(), bucket.getId(), filtered);
        editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(bucketMenu.length() != 0 ? bucketMenu : "no product in bucket");
        editMessageText.setInlineMessageId(callbackQuery.getInlineMessageId());
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        bucketProductService.save(bucketProduct);
        return editMessageText;

    }

    private String getChatId(Update update) {
        return String.valueOf(update.getMessage().getChatId());
    }

    private void getAllProductList(ReplyKeyboardMarkup replyKeyboardMarkup, Lang lang, PageDto<ProductDTO> allProducts) {

        boolean isRu = lang.equals(RU);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();

        KeyboardButton bucket = new KeyboardButton();
        bucket.setText(isRu ? BUCKET_RU : BUCKET_UZ);
        firstRow.add(bucket);
        KeyboardButton search = new KeyboardButton();
        search.setText(isRu ? SEARCH_PRODUCT_RU : SEARCH_PRODUCT_UZ);
        firstRow.add(search);
        rows.add(firstRow);

        KeyboardButton order = new KeyboardButton();
        order.setText(isRu ? ORDER_RU : ORDER_UZ);
        secondRow.add(order);
        KeyboardButton cancel = new KeyboardButton();
        cancel.setText(isRu ? CANCEL_RU : CANCEL_UZ);
        secondRow.add(cancel);
        rows.add(secondRow);

        for (ProductDTO product : allProducts.getItems()) {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton();
            button.setText(product.getName());
            row.add(button);
            rows.add(row);
        }

        replyKeyboardMarkup.setKeyboard(rows);
    }

    private String getProductDescription(Lang lang, Product product) {
        return lang.equals(RU) ? PRODUCT_NAME_RU + ": " + product.getName() + "\n" +
                PRODUCT_DESCRIPTION_RU + ": " + product.getDescription()
                : PRODUCT_NAME_UZ + ": " + product.getName() + "\n" +
                PRODUCT_DESCRIPTION_UZ + ": " + product.getDescription();
    }

    private String getBucketMenu(InlineKeyboardMarkup inlineKeyboardMarkup, Lang lang, Long bucketId, List<BucketProduct> bucketProducts) {

        boolean isRu = lang.equals(RU);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        StringBuilder listOfProduct = new StringBuilder();
        int i = 1;
        for (BucketProduct bucketProduct : bucketProducts) {
            long fullPrice = bucketProduct.getProduct().getPrice() * bucketProduct.getAmount();
            listOfProduct.append(i).append(". ").append(bucketProduct.getProduct().getName()).append(" x ").append(bucketProduct.getAmount()).append(" = ").append(fullPrice).append("\n");
            List<InlineKeyboardButton> productRows = new ArrayList<>();

            InlineKeyboardButton minus = new InlineKeyboardButton();
            minus.setText(MINUS);
            minus.setCallbackData(MINUS + "/" + bucketProduct.getId());
            productRows.add(minus);

            InlineKeyboardButton productName = new InlineKeyboardButton();
            productName.setText(bucketProduct.getProduct().getName());
            productName.setCallbackData("productName");
            productRows.add(productName);

            InlineKeyboardButton plus = new InlineKeyboardButton();
            plus.setText(PLUS);
            plus.setCallbackData(PLUS + "/" + bucketProduct.getId());
            productRows.add(plus);

            InlineKeyboardButton remove = new InlineKeyboardButton();
            remove.setText(REMOVE);
            remove.setCallbackData(REMOVE + "/" + bucketProduct.getId());
            productRows.add(remove);

            rows.add(productRows);
        }

        List<InlineKeyboardButton> clearRow = new ArrayList<>();
        InlineKeyboardButton clearButton = new InlineKeyboardButton();
        clearButton.setText(isRu ? CLEAR_RU : CLEAR_UZ);
        clearButton.setCallbackData(CLEAR + "/" + bucketId);
        clearRow.add(clearButton);
        rows.add(clearRow);

        if (!bucketProducts.isEmpty()) {
            List<InlineKeyboardButton> order = new ArrayList<>();
            InlineKeyboardButton orderButton = new InlineKeyboardButton();
            orderButton.setText(isRu ? ORDER_RU : ORDER_UZ);
            orderButton.setCallbackData(ORDER + "/" + bucketId);
            order.add(orderButton);
            rows.add(order);
        }

        inlineKeyboardMarkup.setKeyboard(rows);

        return listOfProduct.toString().trim();
    }

    private SendMessage orderMenu(Lang lang) {

        boolean isRu = lang.equals(RU);

        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();

        KeyboardButton pickup = new KeyboardButton();
        pickup.setText(isRu ? PICKUP_RU : PICKUP_UZ);
        firstRow.add(pickup);
        KeyboardButton delivery = new KeyboardButton();
        delivery.setText(isRu ? DELIVERY_RU : DELIVERY_UZ);
        firstRow.add(delivery);

        rows.add(firstRow);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setText(isRu ? CHOOSE_DELIVERY_TYPE_RU : CHOOSE_DELIVERY_TYPE_UZ);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    private SendMessage pickUpMenu(List<BucketProduct> bucketProducts, boolean isRu, String chatId) {

        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(isRu ? CONFIRM_RU : CONFIRM_UZ);
        KeyboardButton cancel = new KeyboardButton();
        cancel.setText(isRu ? CANCEL_RU : CANCEL_UZ);
        row.add(button);
        row.add(cancel);
        rows.add(row);
        replyKeyboardMarkup.setKeyboard(rows);

        bucketProducts = bucketProducts.stream().filter(x -> x.getStatus().equals(BucketProductStatus.CHOSEN)).toList();

        StringBuilder listOfProduct = new StringBuilder();
        listOfProduct.append(isRu ? LIST_OF_CHOSEN_PRODUCTS_RU : LIST_OF_CHOSEN_PRODUCTS_UZ).append("\n");
        int i = 1;
        for (BucketProduct bucketProduct : bucketProducts) {
            long fullPrice = bucketProduct.getProduct().getPrice() * bucketProduct.getAmount();
            listOfProduct.append(i).append(". ").append(bucketProduct.getProduct().getName()).append(" x ").append(bucketProduct.getAmount()).append(" = ").append(fullPrice).append("\n");
            i++;
        }

        listOfProduct.append("\uD83D\uDCCD Address: something");

        sendMessage.setChatId(chatId);
        sendMessage.setText(listOfProduct.toString().trim());
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    private SendMessage getLocation(boolean isRu, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(isRu ? SEND_LOCATION_RU : SEND_LOCATION_UZ);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton location = new KeyboardButton();
        location.setRequestLocation(true);
        location.setText(isRu ? LOCATION_RU : LOCATION_UZ);
        row.add(location);
        rows.add(row);

        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

}
