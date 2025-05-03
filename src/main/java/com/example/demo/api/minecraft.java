package com.example.demo.api;

import nl.vv32.rcon.Rcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/API/rcon")
public class minecraft {

    private static final Logger logger = LoggerFactory.getLogger(minecraft.class);

    @Value("${RCON_HOSTNAME}")
    private String rconHostname;

    @Value("${RCON_PORT}")
    private int rconPort;

    @Value("${RCON_PASSWORD}")
    private String rconPassword;

    /**
     * MinecraftサーバーにRCONコマンドを送信して、オンラインプレイヤーの情報を取得します。
     *
     * @return オンラインプレイヤーの情報を含むMap、またはエラーメッセージを含むRconResponse
     */
    @GetMapping("/user")
    public RconResponse sendRconCommand() {
        try (Rcon rcon = Rcon.open(rconHostname, rconPort)) {
            if (rcon.authenticate(rconPassword)) {
                String response = rcon.sendCommand("list");
                logger.info("RCON command response: {}", response); // レスポンスをログ出力

                Pattern patternOnline = Pattern.compile("There are (\\d+) of a max of (\\d+) players online:(.*)");
                Pattern patternOffline = Pattern.compile("There are (\\d+) of a max of (\\d+) players online:");

                Matcher matcherOnline = patternOnline.matcher(response);
                Matcher matcherOffline = patternOffline.matcher(response);
                Map<String, String> result = new HashMap<>();

                if (matcherOnline.matches()) {
                    result.put("onlinePlayers", matcherOnline.group(1));
                    result.put("maxPlayers", matcherOnline.group(2));
                    result.put("players", matcherOnline.group(3).trim());
                } else if (matcherOffline.matches()) {
                    result.put("onlinePlayers", matcherOffline.group(1));
                    result.put("maxPlayers", matcherOffline.group(2));
                    result.put("players", "");
                } else {
                    String errorMessage = "予期しないレスポンス: " + response;
                    logger.warn(errorMessage); // 予期しないレスポンスをログ出力
                    return new RconResponse(errorMessage);
                }

                return new RconResponse(result); // MapをRconResponseでラップして返す

            } else {
                logger.error("RCON authentication failed."); // 認証失敗をログ出力
                return new RconResponse("認証に失敗しました。");
            }
        } catch (IOException e) {
            logger.error("IOException occurred while sending RCON command: {}", e.getMessage()); // 例外をログ出力
            return new RconResponse("例外が発生しました：" + e.getMessage());
        }
    }


    private record RconResponse(Object response) { // レスポンス型を定義
    }

}
