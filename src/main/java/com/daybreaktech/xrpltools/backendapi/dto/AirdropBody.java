package com.daybreaktech.xrpltools.backendapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class AirdropBody {

    private Map<String, List<AirdropItem>> items;

}
