package com.example.demo.controller.zipCode;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class PostController {

    @GetMapping("/list")
    public String home(Authentication authentication, Model model
    ) {
//        populateModelWithUserAttributes(authentication, model);
        String userName = "Guest"; // デフォルトのユーザー名
        Map<String, Object> oauth2info = null;


        if (authentication != null && authentication.isAuthenticated()) {
            userName = authentication.getName();

            if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                populateModelWithUserAttributes(oauthToken, model);
                // OAuth2AuthenticationToken固有の情報へのアクセス
                oauth2info = oauthToken.getPrincipal().getAttributes();
                // OAuth2AuthenticationTokenから名前を取得することも可能
                String oauth2Name = oauthToken.getPrincipal().getAttribute("name");

                model.addAttribute("oauth2info", oauth2info);
                model.addAttribute("oauth2Name", oauth2Name); //OAuth2の名前をmodelに追加
            }

            //  model.addAttribute("principal", authentication.getPrincipal());
        }

        model.addAttribute("username", userName); // Authenticationの名前またはデフォルト値


        return "post/postList";
    }

    private void populateModelWithUserAttributes(OAuth2AuthenticationToken authentication, Model model) {
        DiscordUserAttributes attributes = new DiscordUserAttributes(authentication);
        model.addAttribute("name", attributes.getUsername());
        model.addAttribute("id", attributes.getId());
        model.addAttribute("avatar", attributes.getAvatar());
    }
}

@Getter
class DiscordUserAttributes {
    private final String username;
    private final String id;
    private final String avatar;

    public DiscordUserAttributes(OAuth2AuthenticationToken authentication) {
        this.username = getAttributeFromToken(authentication, "username");
        this.id = getAttributeFromToken(authentication, "id");
        this.avatar = getAttributeFromToken(authentication, "avatar");
    }

    private String getAttributeFromToken(OAuth2AuthenticationToken authentication, String attribute) {
        return (String) authentication.getPrincipal().getAttributes().get(attribute);
    }

}