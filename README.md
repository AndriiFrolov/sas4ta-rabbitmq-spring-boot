# sas4ta-rabbitmq-spring-boot

Task is done in scope of SAS4TA program

Application has a Producer and Consumer that connected via RabbitMQ

Producer sends lowercase message to RabbitMQ randomly every 1 .. 5 seconds (scheduling configured in class SchedulingConfig)
Consumer takes message from queue, makes it upper case and replies back with reply-to https://www.rabbitmq.com/direct-reply-to.html 

