package com.jp.login.entity;

import com.jp.login.Enum.ContentItemList;
import lombok.Data;

@Data
public class ContentItem {
    private Long id;
    private ContentItemList itemName;
}
