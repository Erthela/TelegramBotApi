import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {

    public String getBotUsername() {
        return "Wbeather_bot";
    }

    public String getBotToken() {
        return "1497560729:AAHywJhVGSOG8tCYb3LBWOcMTEnLeK73Z-A";
    }

    public void onUpdateReceived(Update update) {

        try {
            SendMessage message = new SendMessage();
            message.setText(String.valueOf(func(update.getMessage().getText())));
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(str, new
                TypeToken<HashMap<String, Object>>() {
                }.getType());
        return map;
    }

    public static String func(String city) {

        String API_KEY = "542e35e734a9b12ccca3a042d90a5124";
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";


        try {

            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();
            System.out.println(result);

            Map<String, Object> respMap = jsonToMap(result.toString());
            Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
            Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());
            String temp = "Current Temperature: " + mainMap.get("temp") + "\n";
            String hum = "Current Humidity: " + mainMap.get("humidity") + "\n";
            String windSp = "Wind Speed: " + windMap.get("speed") + "\n";
            String windAn = "Wind Angle: " + windMap.get("deg") + "\n";
            System.out.println(temp + hum + windSp + windAn);

            return (temp + hum + windSp + windAn);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

