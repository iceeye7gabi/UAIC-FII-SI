from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad
import socket
from hashlib import md5
from base64 import b64decode
from base64 import b64encode
password_CBC = 'Facultatea de Informatica Iasi'
password_ECB = 'FIIIasi'
socket_client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
socket_client.bind(("127.0.0.1", 23456))
general_password = 'anaaremeremulte'


class AESComponent:
    def __init__(self, key):
        #https://docs.python.org/3.5/library/hashlib.html
        self.my_key = md5(key.encode('utf8')).digest()

    def decrypt_message(self, input_data):
        initial_data = b64decode(input_data)
        self.cipher = AES.new(self.my_key, AES.MODE_CBC, initial_data[:AES.block_size])
        return unpad(self.cipher.decrypt(initial_data[AES.block_size:]), AES.block_size)

    def encrypt_message(self, input_data):
        iv = get_random_bytes(AES.block_size)
        self.cipher = AES.new(self.my_key, AES.MODE_CBC, iv)
        return b64encode(iv + self.cipher.encrypt(pad(input_data.encode('utf-8'),
                                                      AES.block_size)))

def get_encrypted_key(input_password):
    if input_password == "ECB":
        my_decipher = AES.new(general_password.encode("utf-8"), AES.MODE_ECB)
        my_data = my_decipher.encrypt(password_ECB.encode("utf-8"))
    else:
        my_data = AESComponent(general_password).encrypt_message(password_CBC)

    return my_data


if __name__ == '__main__':
    while True:
        initial_data, initial_addr = socket_client.recvfrom(3867)
        my_input = initial_data.decode("utf-8")
        if my_input == "ECB":
            socket_client.sendto(bytes(get_encrypted_key(password_ECB)), initial_addr)
        else:
            socket_client.sendto(bytes(get_encrypted_key(password_CBC)), initial_addr)
