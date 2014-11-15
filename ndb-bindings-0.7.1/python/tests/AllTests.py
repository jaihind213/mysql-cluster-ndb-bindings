import unittest

test_all = unittest.TestLoader().loadTestsFromNames(['tests.ndbapi'])

if __name__=="__main__":
    unittest.TextTestRunner(verbosity=2).run(test_all)


