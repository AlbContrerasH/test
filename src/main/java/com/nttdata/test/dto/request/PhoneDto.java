package com.nttdata.test.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PhoneDto {
    private Long number;
    private int cityCode;
    private int countryCode;
}
