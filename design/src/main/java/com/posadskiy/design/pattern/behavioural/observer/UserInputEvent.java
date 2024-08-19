package com.posadskiy.design.pattern.behavioural.observer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserInputEvent extends Event {
    private String text;
}
