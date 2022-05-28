package project.SPM.mapper;

import project.SPM.dto.CarDTO;
import project.SPM.dto.ViewCarDTO;

import java.util.List;

public interface ICheckMapper {

    // 터치 저장 로직
    boolean saveTouchCheck(List<CarDTO> list, String colNm) throws Exception;

    // 이미지 저장 로직
    List<CarDTO> saveImgCheck() throws Exception;

    // 완료 항목 보기
    List<ViewCarDTO> viewCheck() throws Exception;

}
