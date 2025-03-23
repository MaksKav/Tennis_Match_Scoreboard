package com.maxkavun.dto;

import java.util.List;

public record FinishedMatchResponse(List<FinishedMatchDto> matches, int totalMatches) { }
