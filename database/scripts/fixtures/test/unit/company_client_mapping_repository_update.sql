INSERT INTO company (company_id, name, url_image_logo) VALUES (1, 'A', 'logo');
INSERT INTO client (client_id, phone, can_receive_promo_sms) VALUES (1, 'A', TRUE);
INSERT INTO company_client_mapping (company_client_mapping_id, company_id, client_id, points) VALUES (1, 1, 1, 10)
