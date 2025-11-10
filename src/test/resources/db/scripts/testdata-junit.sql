insert into mandate (active_from, created, deleted, grantee_party_id, grantor_party_id, id,
                     inactive_after, municipality_id, name, namespace, signatory_party_id, updated)
values ('2025-08-01', '2025-10-06 16:34:37.576000', '1970-01-01 01:00:00.000000',
        'fb2f0290-3820-11ed-a261-0242ac120004', 'fb2f0290-3820-11ed-a261-0242ac120002',
        '1a8f4c69-d6b7-4fcb-aa0b-74ed3a2e84a0', '2099-12-31', '2281', 'Ankeborgs Margarinfabrik',
        'MY_NAMESPACE', 'fb2f0290-3820-11ed-a261-0242ac120003', '2025-10-06 16:34:37.576000'),
    
       ('2025-10-01', '2025-10-06 16:36:24.417000', '2025-10-06 16:36:50.598000',
        'fb2f0290-3820-11ed-a261-0242ac120007', 'fb2f0290-3820-11ed-a261-0242ac120005',
        '4f0c18c2-100a-4cb6-a127-e373dc629407', '2099-11-01', '2281', 'Farbror Joakims Bank',
        'MY_NAMESPACE', 'fb2f0290-3820-11ed-a261-0242ac120006', '2025-10-06 16:36:50.600000'),
    
       ('2025-10-01', '2025-10-06 16:37:36.222000', '1970-01-01 01:00:00.000000',
        'fb2f0290-3820-11ed-a261-0242ac120007', 'fb2f0290-3820-11ed-a261-0242ac120005',
        '60850465-28d4-4caa-8e9d-69187461cb27', '2099-11-01', '2281', 'Farbror Joakims Bank',
        'MY_NAMESPACE', 'fb2f0290-3820-11ed-a261-0242ac120006', '2025-10-06 16:37:36.222000');

insert into signing_information (bank_id_issue_date, given_name, id, ip_address, mandate_id, mrtd,
                                 name, ocsp_response, order_ref, personal_number, risk,
                                 signature, created, status, surname, uhi, external_transaction_id)
values ('2020-01-02', 'John', '4354c5a3-4365-44ad-ae81-1ba08a8e470a', '192.168.1.1',
        '1a8f4c69-d6b7-4fcb-aa0b-74ed3a2e84a0', true, 'John Wick', 'YmFzZTY0LWVuY29kZWQgZGF0YQ==',
        '131daac9-16c6-4618-beb0-365768f37288', '200001012384', 'low',
        'YmFzZTY0LWVuY29kZWQgZGF0YQ==', '2025-10-01 15:30:00.000000', 'complete', 'Wick',
        'OZvYM9VvyiAmG7NA5jU5zqGcVpo=', '7923d5fc-b7ef-439d-9343-00b9cb47dde1'),
    
       ('2025-01-02', 'John', '7923d5fc-b7ef-439d-9343-00b9cb47dde1', '192.168.1.2',
        '4f0c18c2-100a-4cb6-a127-e373dc629407', true, 'John Wick', 'YmFzZTY0LWVuY29kZWQgZGF0YQ==',
        '131daac9-16c6-4618-beb0-365768f37288', '200001012384', 'high',
        'YmFzZTY0LWVuY29kZWQgZGF0YQ==', '2025-10-01 15:30:00.000000', 'complete', 'Wick',
        'OZvYM9VvyiAmG7NA5jU5zqGcVpo=', 'f427a6a3-f7d8-4f15-93c6-c886d1c2e0e5'),
    
       ('2025-01-02', 'Kalle', 'f427a6a3-f7d8-4f15-93c6-c886d1c2e0e5', '192.168.1.3',
        '60850465-28d4-4caa-8e9d-69187461cb27', true, 'Kalle Anka', 'YmFzZTY0LWVuY29kZWQgZGF0YQ==',
        '131daac9-16c6-4618-beb0-365768f37288', '200001012384', 'high',
        'YmFzZTY0LWVuY29kZWQgZGF0YQ==', '2025-10-01 15:30:00.000000', 'complete', 'Anke',
        'OZvYM9VvyiAmG7NA5jU5zqGcVpo=', '3e1f4d2b-2c3b-4f4a-8e2d-5f6a7b8c9d0e');
