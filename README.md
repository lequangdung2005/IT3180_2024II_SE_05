# SE-intro-project

## Cài đặt
```bash
git clone https://github.com/lequangdung2005/SE-intro-project.git
cd SE-intro-project
mvn install
```

## Chạy chương trình
```bash
# Đầu tiên chạy server mysql
# Sửa đường url trong src/resources/MySQL.conf cho phù hợp
mvn clean compile
# Bật server
mvn exec:java -Dexec.mainClass="com.hust.ittnk68.cnpm.app.Server"
# Khởi chạy app
mvn exec:java -Dexec.mainClass="com.hust.ittnk68.cnpm.app.Client" -Dexec.args="http://127.0.0.1:8080"
```
