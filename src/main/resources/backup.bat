set mydate=%date:~6,4%%date:~3,2%%date:~0,2%
echo %mydate%
mysqldump -uroot -proot --database db_cda -r C:\backup\%mydate%.sql