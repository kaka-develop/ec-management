# EC-management project

## Developed by Scrum team (group2)
###### Scrum master:
	- Phan Tiến Tùng (tungptseven)
###### Scrum members:
	- Nguyễn Đức Anh (ducanhz135)
	- Đàm Cao Sơn (DamCaoSon)
 	- Nguyễn Hải Nam (dfChicken)
	- Nguyễn Văn Ái (ainguyenkaka)

## Project Management
```
https://ainguyenkaka.visualstudio.com/EnterpriseWebSoftwareDevelopmentSEM7/_dashboards
```
## Project setup
1. Git clone
```
git clone https://github.com/EnterpriseWebSoftwareDevelopmentSEM7-G2/ec-management.git
```
2. Build and test
```
mvn package
```
3. Install mysql and run query for granting user's permission
```
CREATE USER ‘ecm’@‘localhost' IDENTIFIED BY 'ecm';
GRANT ALL ON *.* TO 'ecm'@'localhost';
FLUSH PRIVILEGES;
```
4. Run tomcat web server
```
mvn spring-boot:run
```
## Scrum Log
https://www.gitbook.com/book/tungptseven/scrum-log/details

## Product backlog
https://ainguyenkaka.visualstudio.com/EnterpriseWebSoftwareDevelopmentSEM7/_backlogs?level=Backlog%20items&showParents=false&_a=backlog

### Sprint 1
https://ainguyenkaka.visualstudio.com/EnterpriseWebSoftwareDevelopmentSEM7/_backlogs/Iteration/Sprint%201

### Sprint 2
https://ainguyenkaka.visualstudio.com/EnterpriseWebSoftwareDevelopmentSEM7/_backlogs/TaskBoard/Sprint%202?_a=requirements

