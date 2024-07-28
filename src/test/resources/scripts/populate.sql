TRUNCATE order_items CASCADE;
TRUNCATE products CASCADE;
TRUNCATE orders CASCADE;

INSERT INTO products VALUES
('2a2fbac2-0f95-4cfa-abd2-a50cb0a77227','Red Bottle','Beautiful red bottle with 2L', 100),
('bfb6c824-8dd8-472c-b3e1-7d9eb2ef7c51','Keyboard','Gamer keyboard', 200),
('0d6b2fa2-8345-418d-b401-c47bea559889','MousePad','Red Mousepad gamer ', 100);

INSERT INTO orders VALUES
('a10dfcae-053f-4b0b-97c7-64baafaa3db3', 'order #1', 300, 4),
('536c590d-c08a-498b-b74e-7b76dd82891c', 'order #2', 700, 4);

INSERT INTO order_items
VALUES ('6a28c675-1499-46c5-a404-64c1075c0b37', 0, 400, '536c590d-c08a-498b-b74e-7b76dd82891c', 'bfb6c824-8dd8-472c-b3e1-7d9eb2ef7c51', 2),
       ('9486137b-338e-4629-9da1-f175569a15b0', 100, 300, '536c590d-c08a-498b-b74e-7b76dd82891c', '0d6b2fa2-8345-418d-b401-c47bea559889', 2);