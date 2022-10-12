package com.company;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long chatId;
    private BotState botState = BotState.START;
}
