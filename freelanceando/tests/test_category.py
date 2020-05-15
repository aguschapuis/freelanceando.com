import unittest
from test_base import BaseTest


class CategoryAPITest(BaseTest):

    ENDPOINT = 'api/categories'

    def test_get_categories(self):
        """
        Tests GET /api/categories endpoint. Checks if the objects:

        {"name": "Computer Science Theory", "id": 1}
        {"name": "Cryptography", "id": 2}

        are contained in the response of the server.
        """
        self.check_get_api(self.ENDPOINT, self.categories[0])
        self.check_get_api(self.ENDPOINT, self.categories[1])


if __name__ == '__main__':
    unittest.main()
