import sys
import cv2           # Importing the OpenCV library for computer vision tasks
from cvzone.SelfiSegmentationModule import SelfiSegmentation

def substitude_background(video: str, new_video:str, background: str, cut_threshold: float) :
    segmentor = SelfiSegmentation(model=0)

    segmentor = SelfiSegmentation()

    new_background = cv2.imread(background)
    video = cv2.VideoCapture(video)

    fps = video.get(cv2.CAP_PROP_FPS)

    video_width, video_height = int(video.get(cv2.CAP_PROP_FRAME_WIDTH)), int(video.get(cv2.CAP_PROP_FRAME_HEIGHT))
    out = cv2.VideoWriter(new_video, cv2.VideoWriter_fourcc('V', 'P', '9', '0'), fps, (video_width, video_height))

    new_background = cv2.resize(new_background, (video_width, video_height), interpolation=cv2.INTER_AREA)

    while True:
        success, frame = video.read()
        if not success:
            break
        frame_out = segmentor.removeBG(frame, imgBg=new_background, cutThreshold=cut_threshold)
        out.write(frame_out)
        # cv2.imshow("replace bg", frame_out)
        # if cv2.waitKey(1) & 0xFF == ord('q'):
        #     bre

    video.release()
    out.release()
    cv2.destroyAllWindows()
    
if __name__ == "__main__":
    if len(sys.argv) != 6:
        print("Usage: python your_script.py algName input_file out_file background_file param")
        sys.exit(1) 
    input_file_path = sys.argv[2]
    background_file_path = sys.argv[3]
    output_file_path = sys.argv[4]
    param = int(sys.argv[5])
    params = [0.4, 0.6, 0.8][param]
    substitude_background(input_file_path, output_file_path, background_file_path, params)