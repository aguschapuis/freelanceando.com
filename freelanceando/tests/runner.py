# tests/runner.py
import unittest

import test_freelancer
import test_client
import test_job
import test_pay
import test_category

# initialize the test suite
loader = unittest.TestLoader()
suite = unittest.TestSuite()

# add tests to the test suite
suite.addTests(loader.loadTestsFromModule(test_category))
suite.addTests(loader.loadTestsFromModule(test_freelancer))
suite.addTests(loader.loadTestsFromModule(test_client))
suite.addTests(loader.loadTestsFromModule(test_job))
suite.addTests(loader.loadTestsFromModule(test_pay))

# initialize a runner, pass it your suite and run it
runner = unittest.TextTestRunner(verbosity=3)
result = runner.run(suite)
