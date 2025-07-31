package com.katech.service.quote.mapper;

import com.katech.service.quote.dto.QuoteResponseDto;
import com.katech.service.quote.dto.WorkerDto;
import com.katech.service.quote.entity.Quote;
import com.katech.service.worker.entity.Worker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuoteMapper {

    QuoteResponseDto toQuoteResponseDto(QuoteResponseDto flatData);
}