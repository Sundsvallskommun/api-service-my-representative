insert into mandate (active_from, created, deleted, grantee_party_id, grantor_party_id, id, inactive_after, municipality_id, name, namespace, signatory_party_id, updated)
values  ('2025-08-01', '2025-10-10 14:28:53.462000', '1970-01-01 01:00:00.000000', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c3', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c1', '24b59fba-c6c4-4cec-8723-7d4feb062257', '2099-12-31', '2281', 'Ankeborgs Margarinfabrik', 'MY_NAMESPACE', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c2', '2025-10-10 14:28:53.462000'),
        ('2025-08-01', '2025-10-10 14:32:07.570000', '2025-10-10 14:33:32.009000', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c6', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c4', '62c07c65-a03e-44c4-8505-a39b046bd6d6', '2099-12-31', '2281', 'Weyland Enterprises', 'MY_NAMESPACE', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c5', '2025-10-10 14:33:32.012000'),
        ('2025-10-01', '2025-10-10 14:33:56.429000', '1970-01-01 01:00:00.000000', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c6', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c4', '6a6cc4ae-c873-4f23-932a-680af53197df', '2199-12-31', '2281', 'Weyland Enterprises', 'MY_NAMESPACE', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c5', '2025-10-10 14:33:56.429000'),
        ('2024-10-01', '2025-10-10 14:35:15.505000', '1970-01-01 01:00:00.000000', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c7', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c5', '6f261945-a11c-41b6-8a5a-72ef2b1591de', '2025-06-30', '2281', 'Weyland Enterprises', 'MY_NAMESPACE', 'e47aa4d3-c79a-4d08-a1d2-799ba549e0c6', '2025-10-10 14:35:15.505000');

insert into signing_information (bank_id_issue_date, given_name, id, ip_address, mandate_id, mrtd,
                                 name, ocsp_response, order_ref, personal_number, risk, signature,
                                 created, status, surname, uhi, external_transaction_id)
values ('2021-02-03', 'Donald', '5c31e03e-df74-4600-a3e8-77b97ce2fdbb', '192.168.2.2',
        '6a6cc4ae-c873-4f23-932a-680af53197df', true, 'Donald Duck', 'YmFzZTY0LWVuY29kZWQgZGF0YQ==',
        'a58b727c-75f2-4579-8fed-7e57ad8ec89e', '200001011493', 'low',
        'YmFzZTY0LWVuY29kZWQgZGF0YQ==', '2025-10-01 15:30:00.000000', 'complete', 'Duck',
        'OZvYM9VvyiAmG7NA5jU5zqGcVpo=', 'cef3050a-34d3-4767-9b3e-cbfa65e22a17'),
       ('2020-01-02', 'John', '71dff659-6fbe-48ed-b691-96edee4fe7da', '192.168.1.1',
        '24b59fba-c6c4-4cec-8723-7d4feb062257', true, 'John Wick', 'YmFzZTY0LWVuY29kZWQgZGF0YQ==',
        '131daac9-16c6-4618-beb0-365768f37288', '200001012384', 'low',
        'YmFzZTY0LWVuY29kZWQgZGF0YQ==', '2025-10-01 15:30:00.000000', 'complete', 'Wick',
        'OZvYM9VvyiAmG7NA5jU5zqGcVpo=', '5c31e03e-df74-4600-a3e8-77b97ce2fdbb'),
       ('2021-02-03', 'Donald', '7327f7ff-984f-4180-b16e-98b884abdc37', '192.168.2.2',
        '6f261945-a11c-41b6-8a5a-72ef2b1591de', true, 'Donald Duck', 'YmFzZTY0LWVuY29kZWQgZGF0YQ==',
        'a58b727c-75f2-4579-8fed-7e57ad8ec89e', '200001011493', 'low',
        'YmFzZTY0LWVuY29kZWQgZGF0YQ==', '2025-10-01 15:30:00.000000', 'complete', 'Duck',
        'OZvYM9VvyiAmG7NA5jU5zqGcVpo=', '71dff659-6fbe-48ed-b691-96edee4fe7da'),
       ('2021-02-03', null, 'cef3050a-34d3-4767-9b3e-cbfa65e22a17', '192.168.2.2',
        '62c07c65-a03e-44c4-8505-a39b046bd6d6', true, null, 'YmFzZTY0LWVuY29kZWQgZGF0YQ==',
        'a58b727c-75f2-4579-8fed-7e57ad8ec89e', '200001011493', null,
        'YmFzZTY0LWVuY29kZWQgZGF0YQ==', '2025-10-01 15:30:00.000000', 'complete', null,
        'OZvYM9VvyiAmG7NA5jU5zqGcVpo=', '7327f7ff-984f-4180-b16e-98b884abdc37');
