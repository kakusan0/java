package com.jp.login.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentItemList {
    ホーム("ホーム"),
    ダッシュボード("ダッシュボード"),
    設定("設定");

    private final String value;

    public static ContentItemList fromValue(String value) {
        for (ContentItemList item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown enum value : " + value);
    }
}
