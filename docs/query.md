# Складні запити з поясненнями

```
SELECT
    cl.client_id,
    cl.first_name || ' ' || cl.last_name AS client_name,
    COUNT(c.checkout_id) AS total_checkouts
FROM client cl
JOIN checkout c ON c.client_id = cl.client_id
GROUP BY cl.client_id, cl.first_name, cl.last_name
ORDER BY total_checkouts DESC;
```
Беру всіх клієнтів, рахую кількість їхніх позик книг, об’єдную ім’я та прізвище в client_name і сортую за кількістю їхніх позик.