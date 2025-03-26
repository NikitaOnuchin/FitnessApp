# Приложение для учета калорий

# Запуск приложения:
1. Настройка базы данных:
• Приложение требует подключения к любой пустой базе данных PostgreSQL.
• Заполните файл application.properties в ресурсах, указав параметры подключения.

2. Создание таблиц
• В ресурсах приложения находится SQL-скрипт - CreateDB.sql.
• Скопируйте его содержимое и выполните в консоли базы данных.
• Скрипт создаст необходимые таблицы.
3. Запуск приложения
• Запустите файл FitnessAppApplication.java.

Для удобства Postman коллекция лежит в корне папки. 
Файл - Fitness App Collection.postman_collection.json

# Postman
1. Добавление пользователя
Метод: POST
URL: http://localhost:8080/person
Пример запроса:
   {
   "name": "Viktor",
   "email": "viktor@mail.com",
   "age": 56,
   "weight": 120,
   "height": 200,
   "goal": "WEIGHT_LOSS"
   }
goal принимает следующие значения:
WEIGHT_LOSS – похудение
MAINTENANCE – поддержание веса
WEIGHT_GAIN – набор массы

2. Добавление блюда
Метод: POST
URL: http://localhost:8080/dish
Пример запроса:
   {
   "name": "apple",
   "calories": 52,
   "proteins": 0.3,
   "fats": 0.2,
   "carbohydrates": 14
   }

3. Прием пищи
Метод: POST
URL: http://localhost:8080/meal
Пример запроса:
   {
    "personId": 1,
    "dishIds": [1, 2, 3],
    "mealType": "BREAKFAST"
    }
mealType принимает следующие значения:
BREAKFAST – завтрак
LUNCH – обед
DINNER – ужин

4. Отчеты
4.1. Отчет за день (количество калорий и приемов пищи)
URL: http://localhost:8080/report/sumForDay
Параметры:
• personId 
• date - дата отчета в формате dd-MM-yyyy
Пример URL: http://localhost:8080/report/sumForDay?personId=1&date=20-03-2025
Пример ответа:
   {
    "day": "2025-03-24",
    "sumCalories": 3700,
    "countMeals": 2
    }

4.2. Проверка соответствия дневной норме калорий
URL: http://localhost:8080/report/isWithinCalorieNorm
Параметры:
• personId
• date - дата отчета в формате dd-MM-yyyy
Пример URL: http://localhost:8080/report/isWithinCalorieNorm?personId=1&date=24-03-2025
Пример ответа:
    {
    "withinCalorieNorm": false
    }

4.3. История питания по дням 
URL: http://localhost:8080/report/sumForDays
Параметры:
• personId
• dateFrom - дата отчета от в формате dd-MM-yyyy
• dateTo - дата отчета до в формате dd-MM-yyyy
Пример URL: http://localhost:8080/report/sumForDays?personId=4&dateFrom=24-03-2025&dateTo=25-03-2025
    {
    "daysResponse": [
    {
    "day": "2025-03-24",
    "sumCalories": 0,
    "countMeals": 0
    },
    {
    "day": "2025-03-25",
    "sumCalories": 4500,
    "countMeals": 2
    }
    ]
    }

# Контакты
8-912-826-05-42 Никита
Телеграм - @Onuchin_Nikita
