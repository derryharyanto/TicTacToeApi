package com.test.tictactoeapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerDto {

    private String playerMark;
    private String coordinate;
}
