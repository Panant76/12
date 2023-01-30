package by.vitstep.organizer.service;

import by.vitstep.organizer.model.entity.enums.Currency;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyExchangeService {
    private float tmp = 2;

    public Float exchange(Float amount, Currency from, Currency to) {
        if (from == to) return amount;
        else return amount * tmp;
    }
}
