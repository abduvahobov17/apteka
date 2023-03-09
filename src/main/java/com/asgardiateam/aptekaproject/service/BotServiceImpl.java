package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.Lang;
import com.asgardiateam.aptekaproject.service.interfaces.BotService;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
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
import static com.asgardiateam.aptekaproject.enums.Lang.tryFindLang;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final UserService userService;

    Map<BotState, BiFunction<Update, User, SendMessage>> sendMessageMethods = new ConcurrentHashMap<>();

    {
        sendMessageMethods.put(START, this::greet);
        sendMessageMethods.put(REGISTER_LANG, this::registerLanguage);
        sendMessageMethods.put(REGISTER_PHONE, this::registerPhone);
        sendMessageMethods.put(REGISTER_NAME, this::registerName);
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
        String[] fullName = update.getMessage().getText().trim().toUpperCase().split(" ");
        String firstName = fullName[0];
        String lastName = fullName[1];

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBotState(REGISTER_PHONE);
        userService.save(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(user.getLang().equals(Lang.RU) ? SEND_PHONE_RU : SEND_PHONE_UZ);

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
        String phoneNumber = contact.getPhoneNumber();
        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;
        user.setPhoneNumber(phoneNumber);
        user.setBotState(MAIN_MENU);
        userService.save(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(user.getLang().equals(Lang.RU) ? SUCCESS_REGISTRATION_RU : SUCCESS_REGISTRATION_UZ);
        sendMessage.setChatId(chatId);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(user.getLang().equals(Lang.RU) ? SETTINGS_RU : SETTINGS_UZ);
        row.add(button);
        rows.add(row);
        row = new KeyboardRow();
        button = new KeyboardButton();
        button.setText(user.getLang().equals(Lang.RU) ? SEARCH_PRODUCT_RU : SEARCH_PRODUCT_UZ);
        row.add(button);
        rows.add(row);

        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage search(Update update) {
        return null;
    }

    private String getChatId(Update update) {
        return String.valueOf(update.getMessage().getChatId());
    }

}
