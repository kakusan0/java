package com.jp.login.Enum;

import lombok.Getter;

@Getter
public enum ContentItemList {
    ホーム("ホーム"),
    ダッシュボード("ダッシュボード"),
    設定("設定");

    private String value;

    ContentItemList(String value) {
        this.value = value;
    }
    
    public static ContentItemList fromValue(String value) {
        for (ContentItemList item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown enum value : " + value);
    }

}
