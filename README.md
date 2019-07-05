# LOG Watcher

## Permission

```xml
    <uses-permission android:name="android.permission.READ_LOGS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET"/>
```
## Receiver
```xml
 <receiver android:name=".Alarm"></receiver>
```

## Usage

```java
new GetLogAsyncTask(context).execute();
```
 
- เมื่อเรียกใช้ จะอ่านข้อความจากในไฟล์ .txt ในวันเดียวกัน และ เพิ่ม log ใหม่เข้าไปในไฟล์ 
- clear log ที่เก็บลงไฟล์แล้วทิ้งไป
- หากเป็นวันเดียวกัน แต่ต่างอาทิตย์ จะทำการลบไฟล์ของอาทิตย์ก่อนหน้านี้ 

```java
new SendEmailAsyncTask(context).execute();
```
- ส่ง email ผ่าน SMTP โดยมี host คือ gmail

```java
 public void sendEmail(){
        final String username = "example@gmail.com";
        final String password = "********";
        final String sendto = "example@gmail.com";
        ...
 }
```
สำหรับเปลี่ยนอีเมลผู้รับและผู้ส่ง

### `FileHelper`
อ่านไฟล์ .txt และ รวมเป็นไฟล์ zip

```java 
ReadFile(context)
```
อ่านข้อความจาก .txt และ return String
```java
Zip(context)
```
รวมไฟล์ทั้งหมดที่อยู่ใน directory

### `Alarm`

```java
setAlarm(context)
```
ตั้งเวลาให้เก็บ log ทุกวัน
