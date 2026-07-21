package com.aliu.be.common.constant;

/**
 * Thông báo trả về phổ biến của hệ thống.
 */
public final class MessageConstants {

    private MessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SUCCESS = "Thao tác thành công";
    public static final String CREATED_SUCCESSFULLY = "Tạo mới thành công";
    public static final String UPDATED_SUCCESSFULLY = "Cập nhật thành công";
    public static final String DELETED_SUCCESSFULLY = "Xóa thành công";

    public static final String DATA_NOT_FOUND = "Không tìm thấy dữ liệu";
    public static final String INVALID_REQUEST = "Dữ liệu yêu cầu không hợp lệ";
    public static final String INTERNAL_SERVER_ERROR = "Đã xảy ra lỗi hệ thống";

    public static final String UNAUTHORIZED = "Bạn cần đăng nhập để sử dụng chức năng này";
    public static final String FORBIDDEN = "Bạn không có quyền thực hiện chức năng này";
    public static final String INVALID_TOKEN = "Token không hợp lệ";
    public static final String EXPIRED_TOKEN = "Token đã hết hạn";
}
