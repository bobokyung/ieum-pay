package com.ieum.common.service;

import com.ieum.common.domain.OcrCount;
import com.ieum.common.repository.OcrCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final OcrCountRepository ocrCountRepository;

    public Boolean ocrCount(Long id){
        Optional<OcrCount> exist = ocrCountRepository.findById(id);
        if(exist.isEmpty()){
            ocrCountRepository.save(OcrCount.builder().memberId(id).count(0).build());
            return false;
        }
        if(exist.get().getCount() >= 10)
            return true;
        OcrCount ocrCount = exist.get();
        ocrCount.setCount(ocrCount.getCount() + 1);
        ocrCountRepository.save(ocrCount);
        return false;
    }
}
