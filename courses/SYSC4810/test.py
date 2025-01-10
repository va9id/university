import password, unittest


class TestAssignment(unittest.TestCase):
    def test_password_file_mechanism(self):
        u1, u2, u3 = "vahid", "john doe", "random"
        p1, p2 = "xyz32*#N2", "ClaOs008!"
        r1, r2 = "FA", "CO"

        password.write_to_passwd(u1, p1, r1)
        password.write_to_passwd(u2, p2, r2)

        self.assertTrue(password.check_username_exists(u1))
        self.assertTrue(password.check_username_exists(u2))
        self.assertFalse(password.check_username_exists(u3))

        record1 = password.get_user_record(u1)
        record2 = password.get_user_record(u2)
        record3 = password.get_user_record(u3)

        self.assertEqual(len(record1), 4)
        self.assertEqual(len(record2), 4)
        self.assertEqual(len(record3), 0)

        self.assertEqual(record1[0], u1)
        self.assertEqual(record1[3], r1)

        self.assertEqual(record2[0], u2)
        self.assertEqual(record2[3], r2)

    def test_proactive_password_checker(self):
        exclusions = password.get_exclusions()
        username = "vahid"
        passwords = {
            "Xyza1!": False,
            "Abcuuewq21*3fl": False,
            "Xyzabcdef1!": True,
            "XYZABCDEF1!": False,
            "XyZaBcDeF1!": True,
            "xyzabcdef1!": False,
            "xyzABCdef1*": True,
            "xyzaDcdef!": False,
            "xYzabcdef21%": True,
            "xyzaDc123": False,
            "xyzaDc123$": True,
            "123vahidS!": False,
            "vah1d#N90": True,
            "2000$04%19Ba": False,
            "20John%19": True,
            "Passw0rd!": False,
            "Uhgjd323%*": True,
        }

        for p, expected_result in passwords.items():
            self.assertEqual(
                password.valid_password(username, p, exclusions),
                expected_result,
            )

    def test_user_login(self):
        u, r = "vahid1", "TS"
        real_pas, wrong_pas = "Xyzabcdef1!", "XyzabDEC1!"
        not_real_u, not_real_pass = "notInSystem", "Yuni*#4ds"
        password.write_to_passwd(u, real_pas, r)

        self.assertTrue(password.login(u, real_pas))
        self.assertFalse(password.login(u, wrong_pas))
        self.assertFalse(password.login(not_real_u, not_real_pass))


if __name__ == "__main__":
    unittest.main()
