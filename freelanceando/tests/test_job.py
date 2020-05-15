import copy
import json
import requests
import unittest
from test_base import BaseTest


class JobAPITest(BaseTest):

    ENDPOINT = 'api/jobs'
    CLIENT_ENDPOINT = 'api/clients'

    def _register_client(self, client):
        """Creates a new client and returns the expected object as a copy.
        """
        new_client = copy.deepcopy(client)
        client1_id = self.create_object(self.CLIENT_ENDPOINT, new_client)
        # Add the new id to the original description of client
        new_client['id'] = client1_id
        new_client['job_ids'] = []
        new_client['total_spend'] = 0
        return new_client

    def setUp(self):
        """We create two clients to use during all tests.
        """
        super(JobAPITest, self).setUp()
        self.client1 = self._register_client(self.valid_clients[0])
        self.client2 = self._register_client(self.valid_clients[1])

    def _register_job(self, job, client):
        """Creates a new job and returns the expected object as a copy.
        """
        new_job = copy.deepcopy(job)
        new_job['client_id'] = client['id']

        job1_id = self.create_object(self.ENDPOINT, new_job)
        # Add the new id to the original description of job
        new_job['id'] = job1_id
        return new_job

    def test_post_job(self):
        """
        Tests POST /api/jobs.
        Creates jobs @job1 and @job2, registers them by means
        of the endpoint, and finally checks if they are contained
        in your database by means of a GET.

        @job1 = { "title":"job1", "client_id": 1, "category_id":1,
                "preferred_expertise":"junior", "preferred_country":"AR",
                "hourly_price":40 }
        @job2 = { "title":"job2", "client_id": 2, "category_id":2,
                  "preferred_expertise":"junior", "preferred_country":"AR",
                  "hourly_price":40 }
        """
        job1 = self.valid_jobs[0]
        job2 = self.valid_jobs[1]

        new_job1 = self._register_job(job1, self.client1)
        self.check_get_api(self.ENDPOINT, new_job1)

        new_job2 = self._register_job(job2, self.client1)
        self.check_get_api(self.ENDPOINT,
                           new_job1)
        self.check_get_api(self.ENDPOINT,
                           new_job2)

    def test_post_job_key_id_error(self):
        """
        Tests POST /api/jobs.
        Creates a @job with an existing id and checks if a 400 response code
        is returned by the server.

        @job = { "id":10, "title":"job1", "category_id":1,
                 "client_id":1, "preferred_expertise":"junior",
                 "preferred_country":"AR", "hourly_price":40 }
        """
        registered_job = self._register_job(
            self.valid_jobs[0], self.client1)
        job = self.job_key_id_error
        job['id'] = registered_job['id']

        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(job))

        self.assertEqual(400, response.status_code,
                         msg=(f'Able to register job with existing id '
                              f'Response code: {response.status_code}'))

    def test_post_job_no_category_error(self):
        """
        Tests POST /api/jobs.
        Creates a @job which contains a non existing category_id.
        Checks if a 400 response code is returned by the server.
        """
        non_existing_category_id = len(self.categories) + 1

        job = self.valid_jobs[1]
        job['category_id'] = non_existing_category_id
        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(job))
        self.assertEqual(400, response.status_code,
                         msg=(f'Able to register job with non existing category_id '
                              f'Response code: {response.status_code}'))

    def test_post_job_no_client_error(self):
        """
        Tests POST /api/jobs.
        Creates a @job which contains a non existing client_id.
        Checks if a 400 response code is returned by the server.
        """
        client_id = self.create_object('api/clients', self.valid_clients[0])
        non_existing_client_id = client_id + 1
        job = self.valid_jobs[0]
        job['client_id'] = non_existing_client_id

        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(job))
        self.assertEqual(400, response.status_code,
                         msg=(f'Able to register job with non existing client_id'
                              f'Response code: {response.status_code}'))

    def test_get_jobs(self):
        """
        Tests GET /api/jobs
        Creates a few jobs and as they are created calls the get
        endpoint with the same object to check if it returns it correctly.
        """

        for job in self.valid_jobs:
            registered_job = self._register_job(job, self.client1)
            self.check_get_api(self.ENDPOINT, registered_job)

    def test_get_filtered_jobs(self):
        """
        Tests GET api/jobs/ (filter)
        Registers @job1 and @job2, and requests them and requests all
        the jobs that hold the following conditions:

        {'preferred_country': 'AR'}
        {'preferred_country': 'AR', 'category_id': 1, 'hourly_price': 40}

        Checks if they actually hold the condition and
        if the response contains the expected jobs.

        @job1 = { "title":"job1", "client_id": 1,
                  "category_id":1, "preferred_expertise":"junior",
                  "preferred_country":"AR", "hourly_price":40}
        @job2 = { "title":"job2", "client_id": 2, "category_id":2,
                  "preferred_expertise":"junior", "preferred_country":"AR",
                  "hourly_price":40}
        """
        job1 = copy.deepcopy(self.valid_jobs[0])
        job1['hourly_price'] = 100
        registered_job1 = self._register_job(job1, self.client1)
        registered_job2 = self._register_job(
            self.valid_jobs[1], self.client1)
        registered_job1['preferred_country'] = 'AR'
        registered_job2['preferred_country'] = 'AR'

        condition = {'preferred_country': 'AR'}

        self.check_get_api_filter([registered_job1, registered_job2],
                                  self.ENDPOINT, condition)

        condition = {'preferred_country': 'AR',
                     'category_id': registered_job1['category_id'],
                     'hourly_price': registered_job1['hourly_price']}

        self.check_get_api_filter([registered_job1], self.ENDPOINT, condition)

    def test_list_clients_with_jobs(self):
        client1 = self._register_client(self.valid_clients[0])
        self.check_get_api(self.CLIENT_ENDPOINT, client1)

        job1 = self.valid_jobs[0]
        new_job1 = self._register_job(job1, client1)

        client1['job_ids'] = [new_job1['id']]
        client1_id = client1['id']
        self.check_get_api_id(f'{self.CLIENT_ENDPOINT}/{client1_id}', client1)


if __name__ == '__main__':
    unittest.main()
