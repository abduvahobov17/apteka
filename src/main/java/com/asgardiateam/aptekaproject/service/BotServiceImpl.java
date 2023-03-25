package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.service.interfaces.BotService;
import com.asgardiateam.aptekaproject.service.interfaces.ProductService;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import com.asgardiateam.aptekaproject.utils.Page2Dto;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    Map<BotState, BiFunction<Update, User, SendMessage>> sendMessageMethods = new ConcurrentHashMap<>();

    {
        sendMessageMethods.put(START, this::greet);
        sendMessageMethods.put(REGISTER_LANG, this::registerLanguage);
        sendMessageMethods.put(REGISTER_PHONE, this::registerPhone);
        sendMessageMethods.put(REGISTER_NAME, this::registerName);
        sendMessageMethods.put(MAIN_MENU, this::mainMenu);
        sendMessageMethods.put(SEARCH_PRODUCT_START, this::searchStart);
    }

    @Override
    public SendMessage executeMethod(Update update, User user) {
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

        PageDto<ProductDTO> allProducts = productService.getAll(Pageable.ofSize(5), ProductCriteria.builder().name(productName).build());

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
        String emptyText = lang.equals(RU) ? EMPTY_RU : EMPTY_UZ;
        KeyboardRow row = new KeyboardRow();
        KeyboardButton previous = new KeyboardButton(emptyText);
        KeyboardButton current = new KeyboardButton(String.valueOf(allProducts.getPageNumber()));
        KeyboardButton next = new KeyboardButton(emptyText);
        if (allProducts.isPrevious())
            previous.setText(PREVIOUS);
        if (allProducts.isNext())
            next.setText(NEXT);
        row.addAll(List.of(previous, current, next));
        rows.add(row);
        replyKeyboardMarkup.setKeyboard(rows);
    }

}
