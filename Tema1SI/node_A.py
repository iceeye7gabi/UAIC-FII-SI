from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad
import socket
from hashlib import md5
from base64 import b64decode
from base64 import b64encode
socket_client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
general_password = 'anaaremeremulte'


class AESComponent:
    def __init__(self, key):
        #https://docs.python.org/3.5/library/hashlib.html
        self.my_key = md5(key.encode('utf8')).digest()

    def encrypt_message(self, input_data):
        my_vector = get_random_bytes(AES.block_size)
        self.my_cipher = AES.new(self.my_key, AES.MODE_CBC, my_vector)
        return b64encode(my_vector + self.my_cipher.encrypt(pad(input_data.encode('utf-8'),
                                                                AES.block_size)))

    def decrypt_message(self, input_data):
        initial_data = b64decode(input_data)
        self.my_cipher = AES.new(self.my_key, AES.MODE_CBC, initial_data[:AES.block_size])
        return unpad(self.my_cipher.decrypt(initial_data[AES.block_size:]), AES.block_size)


def message_node_B(message):
    socket_client.sendto(message, ("127.0.0.2", 23457))


def receive_node_B():
    data_node_B, addr_node_B = socket_client.recvfrom(3867)
    return data_node_B


def message_key_manager(message):
    socket_client.sendto(message.encode("utf-8"), ("127.0.0.1", 23456))


def receive_key_manager():
    data_key_manager, addr_key_manager = socket_client.recvfrom(3867)
    return data_key_manager


def encryption_CBC():
    my_data = receive_key_manager()
    output_password = AESComponent(general_password).decrypt_message(my_data).decode("utf-8")
    print(output_password)
    my_data = receive_node_B()
    print(f' B a primit raspuns {str(my_data)}')
    message_node_B("ready".encode("utf-8"))
    output_message = input("Mesaj catre B: ")
    resulted_encrypted_message = AESComponent(output_password).encrypt_message(output_message).decode("utf-8")
    print(f'Mesaj criptat de catre A: {resulted_encrypted_message}')
    message_node_B(resulted_encrypted_message.encode("utf-8"))


def encryption_ECB():
    my_data = receive_key_manager()
    my_decipher = AES.new(general_password.encode("utf-8"), AES.MODE_ECB)
    output_password = my_decipher.decrypt(my_data)
    print(output_password)
    my_data = receive_node_B()
    print(f' B {str(my_data)}')
    message_node_B("ready".encode("utf-8"))
    encryption_block_by_block_ECB(output_password)


def encryption_block_by_block_ECB(password):
    input_message = input("Mesaj(se transmite la b):")
    my_size = len(input_message)
    if len(input_message) % 16 != 0:
        message_node_B(bytes(f'{(my_size // 16) + 1}'.encode("utf-8")))
        print((my_size // 16) + 1)
    else:
        message_node_B(bytes(f'{my_size // 16}'.encode("utf-8")))
        print((my_size // 16))

    while len(input_message) >= 16:
        my_block = input_message[:16]
        curr_message = input_message[16:]
        input_message = curr_message
        print(my_block)
        my_decipher = AES.new(password, AES.MODE_ECB)
        my_data = my_decipher.encrypt(my_block.encode("utf-8"))
        print(str(my_data))
        message_node_B(my_data)

    while len(input_message) < 16:
        print(len(input_message))
        input_message = input_message + " "
    my_block = input_message[:16]
    print(my_block)
    my_decipher = AES.new(password, AES.MODE_ECB)
    my_data = my_decipher.encrypt(my_block.encode("utf-8"))
    print(str(my_data))
    message_node_B(my_data)


if __name__ == '__main__':
    input_message = input("Algoritm:")
    message_node_B(input_message.encode("utf-8"))
    message_key_manager(input_message)
    if input_message == "CBC":
        encryption_CBC()
    else:
        encryption_ECB()
