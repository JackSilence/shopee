目的: 每日定時排程發送蝦皮店家 - 百米家的新上架商品通知信

機制:
將此應用程式部署在Heroku上, 透過UptimeRobot定時監控避免使用Heroku免費帳號的休眠問題

BuyMiJiaTask - 呼叫蝦皮的商品列表及商品明細API取得必要資訊, 透過Gmail及SendGrid發送通知信

SeleniumTask - 透過Selenium + Headless Chrome直接獲取頁面資料後用jsoup parse內容.<br>此Task為備用機制不設定排程, 手動執行只將結果log顯示

ExecuteController - 手動執行Task使用. 將Spring ApplicationContext中符合名稱的Bean取出呼叫execute方法執行.<br>另外加上Swagger UI所需的Annotation以顯示線上文件
