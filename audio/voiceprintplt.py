import matplotlib.pyplot as plt
def draw_voiceprint1(voiceprint):
    fig = plt.figure()
    x=list(i+1 for i in range(512))
    ax = fig.add_subplot(111)
    ax.set(xlim=[0,520], ylim=[-1,1], title='An Example Axes',
           ylabel='Y-Axis', xlabel='X-Axis')
    plt.scatter(x, voiceprint, color='red', marker='+')
    plt.show()

def draw_voiceprint2(voiceprint):
    fig = plt.figure()
    x=list(i+1 for i in range(512))
    ax = fig.add_subplot(111)
    ax.set(xlim=[-0.5,0.5], ylim=[0,60], title='An Example Axes',
           ylabel='Y-Axis', xlabel='X-Axis')
    # plt.scatter(x, voiceprint, color='red', marker='+')
    plt.hist(voiceprint,bins=1000, facecolor="blue", edgecolor="black", alpha=0.7)
    plt.show()