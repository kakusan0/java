package com.jp.login.controller;

import static com.jp.login.constants.ApplicationConstants.ApplicationBase.ROOT;
import static com.jp.login.constants.ApplicationConstants.ApplicationBase.USERNAME;
import static com.jp.login.constants.ApplicationConstants.ApplicationBase.register;
import static com.jp.login.constants.ApplicationConstants.ApplicationRedirectUrl.from_userName_redirect;
import static com.jp.login.constants.ApplicationConstants.ApplicationToUrl.login_to_register;
import static com.jp.login.constants.ApplicationConstants.ApplicationToUrl.login_to_userName;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Profile("dev")
public class RootController {

    @GetMapping(ROOT)
    public String root() {
        return from_userName_redirect;
    }

    @GetMapping(USERNAME)
    public String userName() {
        return login_to_userName;
    }

    @GetMapping(register)
    public String register() {
        return login_to_register;
    }
}

@Controller
@Profile("!dev")
class Dev1Controller {

    @GetMapping(ROOT)
    public String root( // URLから "screenName" パラメータを受け取る。なければ "未選択" になる
            @RequestParam(name = "screenName", defaultValue = "未選択") String screenName,
            Model model) {
        // --- モーダルに表示するリストの準備 ---
        // 選択肢となる画面名のリスト
        List<String> screenList = List.of("ホーム", "ダッシュボード", "設定");
        model.addAttribute("screens", screenList);

        // --- ▼▼▼ ここが最重要ポイント ▼▼▼ ---
        // th:switch で使うための現在の画面名を "currentScreen" という名前でModelに追加
        model.addAttribute("currentScreen", screenName);

        // ボタンに表示するテキストをModelに追加
        String buttonText = "未選択".equals(screenName) ? "画面を選択" : screenName;
        model.addAttribute("selectedScreenName", buttonText);

        // 常にレイアウトの骨組みである test.html を返す
        return "test";
    }
}
