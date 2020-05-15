import json
import os
import requests
import subprocess
import unittest


class BaseTest(unittest.TestCase):
    """Setup to get the server running"""
    BASE_API = 'http://localhost:8080/'
    TEST_DB_DIR = 'test_database'

    def clean_files(self):
        """Removes files from database, except categories.json.
        This won't remove the objects from the database, you need to re-start
        the server for that.
        """
        p = subprocess.check_output(['ls', self.TEST_DB_DIR])
        existing_files = p.decode("utf-8").split('\n')
        # TODO there are waaaay better ways to read the files
        # in a directory
        for filename in existing_files:
            if (filename == '' or 'json' not in filename or
                    filename == 'categories.json' or
                    filename == 'utils.json'):
                continue
            filepath = os.path.join(self.TEST_DB_DIR, filename)
            p = subprocess.call(['rm', filepath])
            if p != 0:
                print(p)  # There was an error, but we can continue
            # Re-create the file with an empty list
            with open(filepath, 'w') as f:
                json.dump([], f)

    def read_categories(self):
        """
        Read categories.json file of the test database and store them
        as objects which will be used for all test cases.
        """
        with open(os.path.join(self.TEST_DB_DIR, 'categories.json'), 'r') as f:
            categories_json = json.load(f)
        self.categories = categories_json

    def read_utils(self):
        """
        Read utils.json file of the test database and store them
        as objects which will be used for all test cases.
        """
        with open(os.path.join(self.TEST_DB_DIR, 'utils.json'), 'r') as f:
            utils_json = json.load(f)

        freelancer = utils_json['freelancer']
        client = utils_json['client']
        job = utils_json['job']

        self.valid_freelancers = freelancer['valid_freelancers']
        self.freelancer_key_id_error = freelancer['key_id_error']
        self.valid_clients = client['valid_clients']
        self.client_key_id_error = client['key_id_error']
        self.valid_jobs = job['valid_jobs']
        self.job_key_id_error = job['key_id_error']

    def setUp(self):
        """
        Set up the objects needed to run test cases before its execution.
        """
        try:
            self.read_categories()
            self.read_utils()
        except IOError:
            raise ValueError("Database could not be read.")

    def tearDown(self):
        # Clean all previous files!
        self.clean_files()

    @staticmethod
    def _check_condition(dict, condition, special_keys=None):
        """
        Given a two dictionaries, @dict and @condition, checks if
        @dict contains all the pairs (key, value) required by @condition.

        @special_keys is a dictionary with attributes that are not equal in
        the response and the condition. Its keys are the condition attribute
        name, and its values are the respose expected attribute name. For each
        of these special keys, we check if the condition value is INSIDE the
        returned value in the response. Use for category_ids.
        """
        # keys that are not equal in response and condition
        if special_keys is None:
            special_keys = {}

        normal_keys_ok = all(dict[key] == value
                             for key, value in condition.items()
                             if key not in special_keys.keys())
        special_keys_ok = all(value in dict[special_keys[key]]
                              for key, value in condition.items()
                              if key in special_keys.keys())
        return normal_keys_ok

    def check_get_api_filter(self, expected_objects, endpoint, condition,
                             special_keys=None):
        """
        Makes a request to the server in order to obtain a list of instances
        of a certain database table filtered by @condition.
        Checks if all the list of objects given by the server holds the
        @condition, and if it contains all the @expected_objects.

        @special_keys is a dictionary with attributes that are not equal in
        the response and the condition. Its keys are the condition attribute
        name, and its values are the respose expected attribute name. For each
        of these special keys, we check if the condition value is INSIDE the
        returned value in the response. Use for category_ids.
        """
        response = requests.get(self.BASE_API + endpoint,
                                params=condition)

        self.assertTrue(
            response.ok,
            msg=f'Error getting {endpoint}: {response.text}')
        objects = json.loads(response.text)

        # Asserts if at least one of the objects given by the server
        # does not hold the condition.
        self.assertTrue(
            all(self._check_condition(dict, condition,
                                      special_keys=special_keys)
                for dict in objects),
            msg=(f'Incorrect objects from {endpoint}\n'
                 f'\tExisting object in {objects} that does not\n'
                 f'\thold the condition {condition}'))

        # Asserts if at least one of the objects in expected_objects
        # is not contained in the list of objects given by the server
        self.assertTrue(
            all((dict in objects) for dict in expected_objects),
            msg=(f'Incorrect objects from {endpoint}\n'
                 f'\t{objects} does not contain all the expected objects\n'
                 f'\t{expected_objects}\n'
                 f'\tCondition: {condition}'))

    def check_get_api(self, endpoint, expected_object):
        """
        Makes a request to the server in order to obtain a list of instances
        of a certain database table.
        Checks if all the list of instances given by the server
        contain the @expected_objects.
        """
        response = requests.get(self.BASE_API + endpoint)
        # response.ok is True if response.status_code  ==  200
        # if reponse.ok: respones.text has the json payload
        self.assertTrue(
            response.ok,
            msg=f'Error getting {endpoint}: {response.text}')
        objects = json.loads(response.text)

        error_msg = (f'Incorrect objects from {endpoint}\n'
                     f'\t{expected_object} is not in\n\t{objects}\n')

        self.assertTrue(expected_object in objects,
                        msg=error_msg)

    def create_object(self, endpoint, object):
        """Calls the post endpoint to create an object and returns its id"""
        response = requests.post(self.BASE_API + endpoint,
                                 data=json.dumps(object))
        self.assertTrue(response.ok,
                        msg=(f'Error register object at endpoint '
                             f'{endpoint} - {response.text}'))
        self.assertTrue(response.text.isdigit(),
                        msg=f'Return value is no int: {response.text}')
        return int(response.text)

    def check_get_api_id(self, endpoint, expected_object):
        """
        Calls the get endpoints by means of an id passed through the
        @endpoint.
        Checks if the instance given by the server is the @expected_object.
        """
        response = requests.get(self.BASE_API + endpoint)

        self.assertTrue(
            response.ok,
            msg=f'Error getting {endpoint}: {response.text}')
        object = json.loads(response.text)

        error_msg = (f'Incorrect objects from {endpoint}\n'
                     f'\t{expected_object} is not in\n\t{object}\n')

        self.assertEqual(expected_object, object, msg=error_msg)


if __name__ == '__main__':
    unittest.main()
