import copy
import json
import requests
import unittest
from test_base import BaseTest


class PayAPITest(BaseTest):

    ENDPOINT = 'api/jobs/pay'
    JOB_ENDPOINT = 'api/jobs'

    def _get_wallets(self, freelancer_id, client_id):
        """
        Given a @freelancer_id and a @client_id requests their instances
        and returns the @total_earning and @total_spend respectively.
        """
        response_freelancer = requests.get(self.BASE_API +
                                           f'api/freelancers/{freelancer_id}')
        response_client = requests.get(self.BASE_API +
                                       f'api/clients/{client_id}')

        registered_freelancer = json.loads(response_freelancer.text)
        registered_client = json.loads(response_client.text)

        freelancer_wallet = registered_freelancer['total_earnings']
        client_wallet = registered_client['total_spend']

        return (freelancer_wallet, client_wallet)

    def _register_job(self, job, client):
        """Creates a new job and returns the expected object as a copy.
        """
        new_job = copy.deepcopy(job)
        new_job['client_id'] = client['id']

        return int(self.create_object(self.JOB_ENDPOINT, new_job))

    def test_post_pay(self):
        """
        Tests POST api/jobs/pay
        Creates a freelancer and a client, gets their ids, and finally
        makes a transaction by means of the endpoint and a @payment.
        Checks if their wallets were saved correctly.

        @payment = {'freelancer_id': <freelancer_id>, 'client_id': <client_id>,
                    'amount': 150}

        Before transaction
          - freelancer's total earnings = 0
          - client's total spend = 0
        After transaction
          - freelancer's total earnings = 150
          - client's total spend = -150
        """
        freelancer = self.valid_freelancers[0]
        client = self.valid_clients[0]
        amount = 150

        client_id = self.create_object('api/clients',
                                       client)
        freelancer_id = self.create_object('api/freelancers',
                                           freelancer)

        client['id'] = client_id
        job1 = self.valid_jobs[0]

        new_job_id = self._register_job(job1, client)
        payment = {'freelancer_id': freelancer_id,
                   'job_id': new_job_id,
                   'amount': amount}

        fwallet, cwallet = self._get_wallets(freelancer_id, client_id)

        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(payment))

        self.assertEqual(200, response.status_code,
                         msg=(f'Error in payment transaction '
                              f'Response code: {response.status_code}'))

        registered_fwallet, registered_cwallet = self._get_wallets(freelancer_id, client_id)

        expected_fwallet = fwallet + amount
        expected_cwallet = cwallet - amount

        self.assertEqual(expected_cwallet,
                         registered_cwallet,
                         msg="Inconsistent client's wallet after payment")

        self.assertEqual(expected_fwallet,
                         registered_fwallet,
                         msg="Inconsistent freelancer's wallet after payment")

    def test_post_pay_freelancer_does_not_exist(self):
        """
        Tests POST api/jobs/pay
        Makes a transaction with a non existing freelancer.
        Checks if the response status code of the server is 400.
        """
        client_id = self.create_object('api/clients',
                                       self.valid_clients[0])
        freelancer_id = self.create_object('api/freelancers',
                                           self.valid_freelancers[0])
        freelancer_id += 1  # Non existing id
        amount = 150

        self.valid_clients[0]['id'] = client_id
        job1 = self.valid_jobs[0]

        new_job_id = self._register_job(job1, self.valid_clients[0])

        payment = {'freelancer_id': freelancer_id,
                   'job_id': new_job_id,
                   'amount': amount}

        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(payment))

        self.assertEqual(400, response.status_code,
                         msg=(f'Able to receive a non existing '
                              f'freelancer in a transaction '
                              f'Response code: {response.status_code}'))

    def test_post_pay_job_does_not_exist(self):
        """
        Tests POST api/jobs/pay
        Makes a transaction with a non existing job.
        Checks if the response status code of the server is 400.
        """
        freelancer_id = self.create_object('api/freelancers',
                                           self.valid_freelancers[0])

        payment = {'freelancer_id': freelancer_id,
                   'job_id': -100,  # Non existing job id
                   'amount': 300}

        response = requests.post(self.BASE_API + self.ENDPOINT,
                                 data=json.dumps(payment))

        self.assertEqual(400, response.status_code,
                         msg=(f'Able to receive a non existing '
                              f'job in a transaction'
                              f'Response code: {response.status_code}'))


if __name__ == '__main__':
    unittest.main()
