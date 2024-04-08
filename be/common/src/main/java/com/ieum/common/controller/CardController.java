package com.ieum.common.controller;

import static com.ieum.common.format.code.FailedCode.REGISTERED_CARD_DELETE;

import com.ieum.common.annotation.CurrentMemberId;
import com.ieum.common.domain.Members;
import com.ieum.common.dto.request.CardMainRequestDTO;
import com.ieum.common.dto.request.CardRegisterRequestDTO;
import com.ieum.common.dto.request.CardUpdateRequestDTO;
import com.ieum.common.dto.response.CardOcrResponseDTO;
import com.ieum.common.format.code.SuccessCode;
import com.ieum.common.format.response.ResponseTemplate;
import com.ieum.common.service.CardService;
import com.ieum.common.service.MemberService;
import com.ieum.common.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "card", description = "card API - 목업")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/card")
public class CardController {

    private final PayService payService;
    private final MemberService memberService;
    private final ResponseTemplate response;
    private final CardService cardService;

    @Operation(summary = "메인 카드 설정", description = "사용자의 메인 카드를 설정합니다.")
    @ApiResponse(responseCode = "200", description = "메인 카드 설정 성공")
    @PutMapping("/main")
    public ResponseEntity<?> cardMain(
        @RequestBody CardMainRequestDTO requestDTO,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        if (payService.checkMyCard(memberId, requestDTO.getRegisterCardId())) {
            memberService.mainCardUpdate(memberId, requestDTO.getRegisterCardId());
            return response.success(SuccessCode.MAIN_CARD_SETTING_SUCCESSFUL);
        }
        return response.success(SuccessCode.MAIN_CARD_SETTING_SUCCESSFUL);
    }

    @Operation(summary = "카드 OCR 처리", description = "카드 이미지를 스캔하여 정보를 추출합니다.")
    @ApiResponse(responseCode = "200", description = "OCR 처리 성공 - OCR 통해 읽은 카드 정보 반환")
    @PostMapping("/ocr")
    public ResponseEntity<?> cardOcr(
        @RequestParam("img") MultipartFile img, @CurrentMemberId Long memberId
    ) {
        if(cardService.ocrCount(memberId)){
            return response.success(CardOcrResponseDTO.builder().build(),SuccessCode.SUCCESS);
        }
        CardOcrResponseDTO cardOcrResponseDTO = payService.getOcr(img);
        return response.success(cardOcrResponseDTO, SuccessCode.CARD_OCR_PROCESS_SUCCESSFUL);
    }

    @Operation(summary = "카드 등록", description = "새로운 카드를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "카드 등록 성공")
    @ApiResponse(responseCode = "500", description = "카드 등록 실패")
    @PostMapping("/register")
    public ResponseEntity<?> cardRegister(
        @RequestBody CardRegisterRequestDTO requestDTO,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        Long id = payService.cardRegister(memberId, requestDTO.getCardNumber(), requestDTO.getCardNickname());

        if (id == -1L) {
            return response.success(id,
                                    SuccessCode.CARD_VALID_WRONG);
        } else if (id == -2L) {
            return response.success(id,
                                    SuccessCode.CARD_TYPE_WRONG);
        }
        Members member = memberService.findMemberById(memberId);
        if (member.getPaycardId() == null) {
            memberService.mainCardUpdate(memberId, id);
        }
        return response.success(id,
                                SuccessCode.CARD_REGISTER_SUCCESSFUL);

    }

    @Operation(summary = "카드 삭제", description = "카드 정보를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "카드 삭제 성공")
    @PutMapping("/update")
    public ResponseEntity<?> cardUpdate(
        @RequestBody CardUpdateRequestDTO requestDTO,
        @Parameter(hidden = true) @CurrentMemberId Long memberId
    ) {
        Members member = memberService.findMemberById(memberId);
        if (member.getPaycardId() == requestDTO.getRegisteredCardId()) {
            return response.error(REGISTERED_CARD_DELETE);
        }
        return response.success(payService.updateCard(memberId, requestDTO.getRegisteredCardId()),
                                SuccessCode.SUCCESS);
    }

    @Operation(summary = "카드 회사", description = "카드 회사 정보를 받습니다.")
    @ApiResponse(responseCode = "200", description = "카드 회사 정보 조회 성공")
    @GetMapping("/{cardNumber}")
    public ResponseEntity<?> getCardCompany(@PathVariable("cardNumber") String number) {
        return response.success(payService.getCardDetail(number),
                                SuccessCode.SUCCESS);
    }
}
