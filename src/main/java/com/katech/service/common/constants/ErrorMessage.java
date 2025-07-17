package com.katech.service.common.constants;

public interface ErrorMessage {

    String EMAIL_ALREADY_EXIST = "Email đã tồn tại";

    String USERNAME_ALREADY_EXIST = "Username đã tồn tại";

    String TAX_CODE_EXISTS = "Mã số thuế đã tồn tại";

    String PHONE_NUMBER_EXISTS = "Số điện thoại đã tồn tại";

    String FROM_DATE_MUST_BE_BEFORE_TO_DATE = "Ngày bắt đầu phải nhỏ hơn ngày kết thúc";

    String PAGE_NUMBER_MUST_BE_POSITIVE = "Số trang phải lớn hơn hoặc bằng 0";

    String PAGE_SIZE_MUST_BE_GREATER_THAN_ZERO = "Kích thước trang phải lớn hơn 0";
}
