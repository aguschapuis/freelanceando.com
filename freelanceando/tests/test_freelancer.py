import copy
import json
import requests
import unittest
from test_base import BaseTest


class FreelancerAPITest(BaseTest):

    ENDPOINT = 'api/freelancers'

    def _register_freelancer(self, freelancer):
        """ Creates a new freelancer and returns the expected object as a copy.
        """
        new_freelancer = copy.deepcopy(freelancer)
        # Remove the extra attribute
        freelancer1_id = self.create_object(self.ENDPOINT, new_freelancer)
        # Add the new id to the original description of freelancer
        new_freelancer['id'] = freelancer1_id
        new_freelancer['total_earnings'] = 0
        return new_freelancer

    def test_post_freelancer(self):
        """
        Tests POST /api/freelancers.
        Creates freelancers @freelancer1 and @freelancer2, registers them
        by means of the endpoint, and finally checks if they are contained
        in the database of the server by means of a GET.

        @freelancer1 = { "username": "ada_lovelace", "hourly_price": 100,
                         "country_code": "EN", "reputation": "expert",
                         "category_ids": [1, 2] }
        @freelancer2 = { "username": "alan_turing", "hourly_price": 100,
                         "country_code": "EN", "reputation": "expert",
                         "category_ids": [1]}
        """

        freelancer1 = self.valid_freelancers[0]
        freelancer2 = self.valid_freelancers[1]

        new_freelancer1 = self._register_freelancer(freelancer1)
        self.check_get_api(self.ENDPOINT, new_freelancer1)

        new_freelancer2 = self._register_freelancer(freelancer2)
        self.check_get_api(self.ENDPOINT,
                           new_freelancer1)
        self.check_get_api(self.ENDPOINT,
                           new_freelancer2)

    def test_post_freelancer_no_category_error(self):
        """
        Tests POST /api/freelancers.
        Creates a @freelancer which contains non existing category_ids.
        Checks if a 400 response code is returned by the server

        @freelancer = { "username": "ada_lovelace", "hourly_price": 100,
                        "country_code": "EN", "reputation": "expert",
                        "category_ids": [1, 5] }
        """
        freelancer = self.valid_freelancers[2]
        new_freelancer = copy.copy(freelancer)
        new_freelancer['category_ids'] = [1, 5]  # Non existing category

        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(new_freelancer))

        self.assertEqual(
            400, response.status_code,
            msg=(f'Able to register freelancer with non-existing category. '
                 f'Response code: {response.status_code}'))

    def test_post_freelancer_key_id_error(self):
        """
        Tests POST /api/freelancers.
        Creates a freelancer with an existing id, checks if the server returns
        a 400 response code.

        @freelancer = { "id": 1, "username":"ada_lovelace",
                        "hourly_price": 100, "country_code":"EN",
                        "reputation":"expert", "category_ids": [1, 2]}
        """
        registered_freelancer = self._register_freelancer(
            self.valid_freelancers[0])
        freelancer = self.freelancer_key_id_error
        freelancer['id'] = registered_freelancer['id']

        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(freelancer))

        self.assertEqual(400, response.status_code,
                         msg=(f'Able to register freelancer with existing id '
                              f'Response code: {response.status_code}'))

    def test_get_freelancers(self):
        """
        Tests GET /api/freelancers
        Creates a few freelancers and as they are created calls the get
        endpoint with the same object to check if it returns it correctly.
        """
        for freelancer in self.valid_freelancers:
            registered_freelancer = self._register_freelancer(freelancer)
            self.check_get_api(self.ENDPOINT, registered_freelancer)

    def test_get_freelancer(self):
        """
        Tests GET /api/freelancers/:id
        Registers a @freelancer then calls the get endpoint with its
        id and checks if it returns it correctly

        @freelancer = { "username": "ada_lovelace", "hourly_price": 100,
          "country_code": "EN", "reputation": "expert",
          "category_ids": [1, 2] }
        """

        registered_freelancer = self._register_freelancer(
            self.valid_freelancers[0])

        id = registered_freelancer['id']

        self.check_get_api_id(f'{self.ENDPOINT}/{id}', registered_freelancer)

    def test_get_freelancer_not_an_int_error(self):
        """
        Tests GET /api/freelancers/:id
        Requests a freelancer by means of a non integer paremeter as id
        Checks if the server returns 400 response code.
        """
        response = requests.get(self.BASE_API + f'{self.ENDPOINT}/uno')

        self.assertEqual(400, response.status_code,
                         msg=(f'Able to recive a non integer through the endpoint '
                              f'Response code: {response.status_code}'))

    def test_get_freelancer_does_not_exist_error(self):
        """
        Tests GET /api/freelancers/:id
        Requests a freelancer by means of a non existing id.
        """
        registered_freelancer = self._register_freelancer(
            self.valid_freelancers[0])

        non_existing_id = registered_freelancer['id'] + 1
        endpoint = f'{self.ENDPOINT}/{non_existing_id}'

        response = requests.get(self.BASE_API + endpoint)

        self.assertEqual(400, response.status_code,
                         msg=(f'Able to get a non existing freelancer '
                              f'Response code: {response.status_code}'))

    def test_get_filtered_freelancers(self):
        """
        Tests GET api/freelancers (filter)
        Registers @freelancer1 and @freelancer2, and requests all
        the freelancers that hold the following conditions:

        {'country_code': 'AR'}
        {'country_code': 'AR', 'category_id': 1, 'reputation': 'junior'}

        Checks if they actually hold the condition and
        if the response contains the expected freelancers.

        @freelancer1 = { "username": "ada_lovelace", "hourly_price": 100,
                         "country_code": "EN", "reputation": "junior",
                         "category_ids": [1, 2] }
        @freelancer2 = { "username": "alan_turing", "hourly_price": 100,
                         "country_code": "EN", "reputation": "expert",
                         "category_ids": [1]}

        *The category_id of the second condition belongs to the list
        of categories inside of @freelancer1
        """
        freelancer1 = self.valid_freelancers[0]
        freelancer2 = self.valid_freelancers[1]
        freelancer3 = self.valid_freelancers[2]
        freelancer1['country_code'] = 'AR'
        freelancer1['reputation'] = 'junior'
        freelancer2['country_code'] = 'AR'
        freelancer2['category_ids'] = [2]
        freelancer3['country_code'] = 'EN'
        freelancer3['reputation'] = 'junior'

        registered_freelancer1 = self._register_freelancer(freelancer1)
        registered_freelancer2 = self._register_freelancer(freelancer2)
        registered_freelancer3 = self._register_freelancer(freelancer3)

        condition = {'country_code': 'AR'}

        special_keys = {"category_id": "category_ids"}
        self.check_get_api_filter(
            [registered_freelancer1, registered_freelancer2],
            self.ENDPOINT, condition, special_keys=special_keys)

        condition = {'country_code': 'AR',
                     'category_id': 1,
                     'reputation': 'junior'}

        self.check_get_api_filter(
            [registered_freelancer1], self.ENDPOINT, condition,
            special_keys=special_keys)


if __name__ == '__main__':
    unittest.main()
