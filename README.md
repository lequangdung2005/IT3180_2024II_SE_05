# SE-intro-project

## Cài đặt
```bash
git clone https://github.com/lequangdung2005/IT3180_2024II_SE_05.git
cd SE-intro-project
mvn install
```

## Chạy chương trình
```bash
mvn clean compile
# Bật server
mvn exec:java -D exec.mainClass="com.hust.ittnk68.cnpm.app.Server"
# Khởi chạy app
mvn exec:java -D exec.mainClass="com.hust.ittnk68.cnpm.app.Client" -D exec.args="http://127.0.0.1:8080"
```
> [!NOTE]
>
> Phải chạy Server và Client trên 2 terminal riêng, thường server sẽ mở tại cổng 8080.
> Nếu Server mở tại cổng khác hãy sửa đổi tham số của câu lệnh cuối.
