CREATE TABLE IF NOT EXISTS clearing_cost(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_code VARCHAR(5)     NOT NULL,
    cost         DECIMAL(10, 2) NOT NULL
);
