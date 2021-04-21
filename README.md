# Telegram_Clone

- Thông tin thành viên nhóm:
  - | Name                 | ID       |
    | -------------------- | -------- |
    | Nguyễn Phú Trung Anh | 18120276 |
    | Võ Thế Minh          | 18120211 |
    | Lê Đức Thành         | 18120238 |
    | Nguyễn Thị Ngọc Trâm | 18120246 |
    | Phạm Tống Bình Minh  | 18120210 |
- Thông tin liên hệ: minhthevo123@gmail.com

## Mô tả đề tài

- Actor chính: Người dùng
- Dữ liệu cần quản lí: Thông tin người dùng, danh bạ, lịch sử nhắn tin(gọi, video call)
- Các chức năng dự định của người dùng:
  - Đăng nhập xác thực OTP, đăng xuất(hoàn thành)
  - Nhắn tin, gửi sticker, emoji(hoàn thành)
  - Nghe gọi(hoàn thành)
  - Video call(đang làm)
  - Voice record(đang làm)
  - Gửi file, hình ảnh(đang làm)
  - Thay đổi thông tin cá nhân(Tên đại diện, ảnh đại diện, miêu tả, ...)(hoàn thành)
  - Thêm người dùng khác vào danh bạ(hoàn thành)
  - Tạo nhóm chat(đang làm)

## Clone về máy và chạy thử

- Chuẩn bị

  1. Cài đặt [Android studio](https://developer.android.com/studio?gclid=Cj0KCQjw9_mDBhCGARIsAN3PaFPaGHhhpnTjRgyZD6vZ3Eft6XYLerm2jI_Z7Qd5GvQKlczvzSncLnkaAk-2EALw_wcB&gclsrc=aw.ds "Anrdoid studio")
  2. Điện thoại hệ điều hành `android 6.0` trở lên
  3. Bật chế độ `Debugging mode` trên điện thoại
  4. Kết nối điện thoại và máy tính bằng dây cáp có hỗ trợ Debugging mode

- Chạy thử
  1. Sử dụng Android studio mở project tại đương dẫn `./messege_clone`
  2. Tạo tài khoản, tạo project Firebase đặt tên là `message_clone`
  3. Lấy mã `SHA-256` hoặc `SHA-1` ở mục `./message_clone/Tasks/android/signingReport`
  4. Đăng nhập Firebase chọn project `message_clone` vào mục `Project setting` thêm mã `SHA-256/SHA`-1 ở mục `Android apps`
  5. Ở mục `Debug/running mode` chọn `app`
  6. Ở mục `Running device` chọn điện thoại bạn kết nối
  7. `Shift+F10` hoặc chọn `run App`

## **Demo**

### **Giao diện chính của ứng dụng**

<div>

</div>
<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008608/Android_telegram_readme_resources/photos/giaodien1_zazykr.png" width="250" height="500" rightmargin="50px" leftmargin="50px">

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008608/Android_telegram_readme_resources/photos/giaodien2_vx7vtx.png" width="250" height="500" margin-left="50px">

<div>
  <div id="light-mode" class="inline-text" >Light mode</div> <div id="dark-mode" class="inline-text">Dark mode</div>
</div>

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008609/Android_telegram_readme_resources/photos/slidebar_hxmi5s.png" width ="250" height="500">

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008609/Android_telegram_readme_resources/photos/lienhe_hutvg1.png" width ="250" height="500">

<div>
  <div id="slide-bar" class="inline-text" >Slide bar</div> <div id="dark-mode" class="inline-text">Contact</div>
</div>

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008609/Android_telegram_readme_resources/photos/taikhoancanhan_lhznui.png" width ="250" height="500">

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008609/Android_telegram_readme_resources/photos/manhinhnhantin_siy56u.png" width ="250" height="500">

<div>
  <div id="profile" class="inline-text" >Profile</div> <div id="dark-mode" class="inline-text">Chat screen</div>
</div>

### **Đăng nhập bằng số điện thoại**

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008608/Android_telegram_readme_resources/photos/dangnhap1_jc1tac.png" width ="250" height="500">
<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008608/Android_telegram_readme_resources/photos/dangnhap2_iyevyt.png" width ="250" height="500">
<div>
  <div class="inline-text"></div>
</div>

### **Thay đổi, cập nhật thông tin cá nhân**
<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008609/Android_telegram_readme_resources/photos/thongtintaikhoan_h8g52c.png" width ="250" height="500">

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008610/Android_telegram_readme_resources/photos/chonanhdaidien_bbf8tc.png" width ="250" height="500">

<div>
  <div id="origin-profile" class="inline-text" >Origin profile</div> <div id="dark-mode" class="inline-text">Update avatar</div>
</div>

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008611/Android_telegram_readme_resources/photos/thaydoiten_ndyw4l.png" width ="250" height="500">

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008611/Android_telegram_readme_resources/photos/daluuthongtin_pbh0za.png" width ="250" height="500">
<div>
  <div id="update-name" class="inline-text" >Update name</div> <div id="dark-mode" class="inline-text">After changed</div>
</div>

### **Thực hiện cuộc gọi thoại**
<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008610/Android_telegram_readme_resources/photos/goithoai1_qtoxcz.png" width ="250" height="500">

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008613/Android_telegram_readme_resources/photos/goithoai2_anvuaa.png" width ="250" height="500">

<div>
  <div class="inline-text"></div>
</div>

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008611/Android_telegram_readme_resources/photos/goithoai3_ttf9nn.png" width ="250" height="500"> 

<img class="giaodien" src="https://res.cloudinary.com/teamwebctt2/image/upload/v1619008610/Android_telegram_readme_resources/photos/goithoai4_d6douy.png" width ="250" height="500">
<div>
  <div class="inline-text"></div>
</div>

## Công nghệ được sử dụng trong đồ án

- Phía giao diện GUI
  - Sử dụng ngôn ngữ thiết `Material` của `Google`
  - Hỗ trợ Ligh mode/Dark mode
  - Tone màu chủ đạo: `#0353A4`
  - Ngôn ngữ: tiếng anh hướng tới đa ngôn ngữ
- Phía back-end
  - Cơ sở dữ liệu: `Firebase realtime database`
  - Xác thực đăng nhập: `Firebase authentication`
  - Server gọi thoại: `Sinch call server`
  - Quản lý danh sách: `Recycle view`
  - Lưu trữ tài nguyên: `Firebase storage`
  - Live data: `Live data`
  
