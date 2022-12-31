package com.Fawry.app.models;

import com.Fawry.app.helperClasses.payment.Card;
import com.Fawry.app.helperClasses.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayRequest {
    Map<String, String> handler = new HashMap<>();
    Card card= new Card();
}
