import numpy as np
import torchvision.models as models
from torch.autograd import Variable
import torchvision.transforms as transforms

from PIL import Image

TARGET_IMG_SIZE = 224

transform_list = [transforms.ToTensor(),
                  transforms.Normalize(mean=[0.485, 0.456, 0.406],
                                       std=[0.229, 0.224, 0.225])]
img_to_tensor = transforms.Compose(transform_list)


def make_model():
    resmodel = models.vgg16(pretrained=True)
    return resmodel


# 分类
def inference(resmodel, imgpath):
    resmodel.eval()  # 必需，否则预测结果是错误的

    img = Image.open(imgpath).convert("RGB")
    img.resize((TARGET_IMG_SIZE, TARGET_IMG_SIZE))
    tensor = img_to_tensor(img)
    tensor = tensor.resize_(1, 3, TARGET_IMG_SIZE, TARGET_IMG_SIZE)
    result = resmodel(Variable(tensor))
    result_npy = result.data.cpu().numpy()
    max_index = np.argmax(result_npy[0])

    return max_index


# 特征提取
def extract_feature(resmodel, imgpath):
    resmodel.eval()

    img = Image.open(imgpath).convert("RGB")
    img = img.resize((TARGET_IMG_SIZE, TARGET_IMG_SIZE))
    tensor = img_to_tensor(img)

    tensor = tensor.resize_(1, 3, TARGET_IMG_SIZE, TARGET_IMG_SIZE)

    result = resmodel(Variable(tensor))
    result_npy = result.data.cpu().numpy()

    return result_npy[0]


if __name__ == "__main__":

    A = np.array([[1, 1, 1], [1, 2, 3], [3, 3, 3]])

    B = np.array([[1, 3, 2], [4, 6, 2], [1, 5, 8]])

    print("A = ", A)
    print("B = ", B)

    C = A.dot(B)
    print("A dot B = ", C)

    D = A.__matmul__(B)
    print("A matmul B = ", D)

    E = np.matmul(A, B)
    print("A matmul B = ", E)

    print(A[0, :])
    print(A[:, 0])




