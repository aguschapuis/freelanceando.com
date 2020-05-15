import copy
import json
import requests
import unittest
from test_base import BaseTest


class ClientAPITest(BaseTest):

    ENDPOINT = 'api/clients'

    def _register_client(self, client):
        """Creates a new client and returns the expected object as a copy.
        """
        new_client = copy.deepcopy(client)
        client1_id = self.create_object(self.ENDPOINT, new_client)
        # Add the new id to the original description of client
        new_client['id'] = client1_id
        new_client['job_ids'] = []
        new_client['total_spend'] = 0
        return new_client

    def test_post_client(self):
        """
        Tests POST /api/clients.
        Creates clients @client1 and @client2, registers them by means
        of the endpoint, and finally checks if they are contained
        in the database of the server by means of a GET.

        @client1 = { "username":"client1", "country_code":"AR"}
        @client2 = { "username":"client2", "country_code":"AR"}
        """
        client1 = self.valid_clients[0]
        client2 = self.valid_clients[1]

        new_client1 = self._register_client(client1)
        self.check_get_api(self.ENDPOINT, new_client1)

        new_client2 = self._register_client(client2)
        self.check_get_api(self.ENDPOINT, new_client1)
        self.check_get_api(self.ENDPOINT, new_client2)

    def test_post_client_key_id_error(self):
        """
        Tests POST /api/clients.
        Creates a @client with an existing id and checks if the server returns
        a 400 response code.

        @client = { "id": 1, "username":"client1", "country_code": "AR" }
        """
        registered_client = self._register_client(
            self.valid_clients[0])
        client = self.client_key_id_error
        client['id'] = registered_client['id']

        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(client))

        self.assertEqual(400, response.status_code,
                         msg=(f'Able to register client with existing id'
                              f'Response code: {response.status_code}'))

    def test_get_clients(self):
        """
        Tests GET /api/clients
        Creates a few clients and as they are created calls the get endpoint
        with the same object to check if it returns it correctly.
        """
        for client in self.valid_clients:
            registered_client = self._register_client(client)
            self.check_get_api(self.ENDPOINT, registered_client)

    def test_get_client(self):
        """
        Tests GET /api/clients/:id
        Creates a new client, then calls the get endpoint with the same
        object to check if it returns it correctly.
        """

        registered_client = self._register_client(
            self.valid_clients[0])

        id = registered_client['id']

        self.check_get_api_id(f'{self.ENDPOINT}/{id}', registered_client)

    def test_get_client_not_an_int_error(self):
        """
        Tests GET /api/clients/:id
        Requests a client by means of a non integer paremeter as id.
        Checks if the server returns 400 response code.
        """
        response = requests.get(self.BASE_API + f'{self.ENDPOINT}/one')

        self.assertEqual(400, response.status_code,
                         msg=(f'Able to recive a non integer through the endpoint '
                              f'Response code: {response.status_code}'))

    def test_get_client_does_not_exist_error(self):
        """
        Tests GET /api/clients/:id
        Requests a client by means of a non existing id.
        Checks if the server returns 400 response code
        """
        registered_client = self._register_client(
            self.valid_clients[0])

        non_existing_id = registered_client['id'] + 1   # Incremental id's
        endpoint = f'{self.ENDPOINT}/{non_existing_id}'

        response = requests.get(self.BASE_API + endpoint)

        self.assertIn(
            response.status_code, [400, 404],
            msg=(f'Able to recive a non integer through the endpoint '
                 f'Response code: {response.status_code}'))


if __name__ == '__main__':
    unittest.main()
