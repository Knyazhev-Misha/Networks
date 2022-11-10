# О программе
* Программу можно скачать командой git clone [link](https://github.com/Knyazhev-Misha/Networks.git) 
* Сборка mvn compile
* Исполнение mvn exec:java -Dexec.mainClass="com.server(client).Main"
* Исполнение с входными аргументами -Dexec.args="arg0 arg1 arg2"
* FileSharingClint получает на вход абсолютный путь, ip и порт сервера, на выход передается состояние передачи файла, успех или нет
* FileSharingServer получает на вход порт, сохраняет в папке upliads файл(удалит его если размер файла не совпадет с полученным размером), и отвечает клиенты успешно ли передался файл