package com.yourroom.space.dto;

import java.math.BigDecimal;

public class SpaceBasicsRequest {
    public Long ownerId;           // MVP: lo pasamos en body; luego lo sacamos del token
    public String title;           // requerido
    public String location;
    public String addressLine;
    public Integer capacity;
    public BigDecimal hourlyPrice; // requerido



}
