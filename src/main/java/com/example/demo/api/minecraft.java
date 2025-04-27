package com.example.demo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vv32.rcon.Rcon;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class minecraft {

    @GetMapping("/rcon")
    public String sendRconCommand(
            @RequestParam String hostname,
            @RequestParam int port,
            @RequestParam String password,
            @RequestParam(defaultValue = "list") String command
    ) {
        try (Rcon rcon = Rcon.open(hostname, port)) {

            if (rcon.authenticate(password)) {
                String response = rcon.sendCommand(command);
                System.out.println(response);

                if (command.equals("list")) {
                    // プレイヤーオンライン時のパターン
                    Pattern patternOnline = Pattern.compile("There are (\\d+) of a max of (\\d+) players online:(.*)");
                    // プレイヤーオフライン時のパターン
                    Pattern patternOffline = Pattern.compile("There are (\\d+) of a max of (\\d+) players online:");

                    Matcher matcherOnline = patternOnline.matcher(response);
                    Matcher matcherOffline = patternOffline.matcher(response);
                    Map<String, String> result = new HashMap<>();

                    if (matcherOnline.matches()) {
                        result.put("onlinePlayers", matcherOnline.group(1));
                        result.put("maxPlayers", matcherOnline.group(2));
                        result.put("players", matcherOnline.group(3).trim()); // プレイヤーリスト部分をトリム
                    } else if (matcherOffline.matches()) {
                        result.put("onlinePlayers", matcherOffline.group(1));
                        result.put("maxPlayers", matcherOffline.group(2));
                        result.put("players", ""); // プレイヤーがいない場合は空文字列
                    } else {
                        return "予期しないレスポンス:" + response; // デバッグ用にレスポンス全文を返す
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        return objectMapper.writeValueAsString(result);
                    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                        return "JSON変換に失敗しました。";
                    }
                }

                return response;

            } else {
                return "認証に失敗しました。";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}